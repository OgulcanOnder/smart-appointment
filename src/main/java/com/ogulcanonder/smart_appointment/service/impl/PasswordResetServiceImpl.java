package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.dto.request.PasswordResetRequestDTO;
import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.exception.MailSendException;
import com.ogulcanonder.smart_appointment.exception.PasswordResetTokenExpiredException;
import com.ogulcanonder.smart_appointment.service.AccountService;
import com.ogulcanonder.smart_appointment.service.PasswordResetService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

@Service
@Transactional(readOnly = true)
public class PasswordResetServiceImpl implements PasswordResetService {
    private final RedisTemplate<String, String> redisTemplate;
    private final AccountService accountService;
    private final JavaMailSender javaMailSender;

    private static final int RESET_TOKEN_LENGTH = 32;
    private static final int RESET_TOKEN_TTL_MINUTES = 15;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    private final String passwordResetUrl;

    public PasswordResetServiceImpl(RedisTemplate<String, String> redisTemplate, AccountService accountService,
                                    JavaMailSender javaMailSender,
                                    @Value("${password.reset.url}") String passwordResetUrl) {
        this.redisTemplate = redisTemplate;
        this.accountService = accountService;
        this.javaMailSender = javaMailSender;
        this.passwordResetUrl = passwordResetUrl;
    }

    @Override
    public void processRequest(String email) {
        Account account = accountService.findByEmail(email);
        if (ObjectUtils.isEmpty(account)) {
            return;
        }
        String existingToken = redisTemplate.opsForValue().get(buildPasswordResetEmailKey(email));
        if (existingToken != null) {
            redisTemplate.delete(buildPasswordResetTokenKey(existingToken));
        }
        String token = generateResetToken();
        redisTemplate.opsForValue().set(
                buildPasswordResetTokenKey(token), email, Duration.ofMinutes(RESET_TOKEN_TTL_MINUTES));
        redisTemplate.opsForValue().set(
                buildPasswordResetEmailKey(email), token, Duration.ofMinutes(RESET_TOKEN_TTL_MINUTES));
        sendEmail(email, token);
    }

    @Transactional
    @Override
    public void resetPassword(PasswordResetRequestDTO passwordResetRequestDTO) {
        String email = redisTemplate.opsForValue().get(buildPasswordResetTokenKey(passwordResetRequestDTO.token()));
        if (ObjectUtils.isEmpty(email)) {
            throw new PasswordResetTokenExpiredException("Token is expired");
        }
        Account account = accountService.findByEmail(email);
        accountService.resetPassword(account, passwordResetRequestDTO);
        redisTemplate.delete(buildPasswordResetTokenKey(passwordResetRequestDTO.token()));
        redisTemplate.delete(buildPasswordResetEmailKey(email));
    }

    private String generateResetToken() {
        byte[] randomBytes = new byte[RESET_TOKEN_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return encoder.encodeToString(randomBytes);
    }

    private String buildPasswordResetTokenKey(String token) {
        return "password_reset_token:" + token;
    }

    private String buildPasswordResetEmailKey(String email) {
        return "password_reset_email:" + email;
    }

    private void sendEmail(String email, String token) {
        String resetUrl = passwordResetUrl + token;
        String mailBody = """
                <!DOCTYPE html>
                        <html>
                        <body style="margin:0; padding:0; background-color:#f4f4f4; font-family:Arial,sans-serif;">
                        <table width="100%%" cellpadding="0" cellspacing="0">
                        <tr>
                        <td align="center" style="padding:40px 0;">
                        <table width="600" cellpadding="0" cellspacing="0"
                style="background-color:#ffffff; border-radius:8px; overflow:hidden;">
                
                        <!-- Header -->
                        <tr>
                        <td style="background-color:#4F46E5; padding:32px; text-align:center;">
                        <h1 style="color:#ffffff; margin:0; font-size:24px;">Smart Appointment</h1>
                        </td>
                        </tr>
                
                        <!-- Body -->
                        <tr>
                        <td style="padding:40px 32px;">
                        <h2 style="color:#1a1a1a; margin:0 0 16px;">Password Reset Request</h2>
                        <p style="color:#555555; line-height:1.6; margin:0 0 24px;">
                        We received a request to reset your password.
                        Click the button below to create a new password.
                        </p>
                
                        <!-- Button -->
                        <table cellpadding="0" cellspacing="0">
                        <tr>
                        <td style="background-color:#4F46E5; border-radius:6px;">
                        <a href="%s"
                style="display:inline-block; padding:14px 32px;
                color:#ffffff; text-decoration:none;
                font-weight:bold; font-size:15px;">
                Reset Password
                                                        </a>
                        </td>
                        </tr>
                        </table>
                
                        <!-- Warning -->
                        <p style="color:#888888; font-size:13px; margin:24px 0 0; line-height:1.6;">
                                                ⚠️ This link will expire in <strong>15 minutes</strong>.<br>
                        If you didn't request a password reset, you can safely ignore this email.
                        </p>
                        </td>
                        </tr>
                
                        <!-- Footer -->
                        <tr>
                        <td style="background-color:#f9f9f9; padding:24px 32px; text-align:center;
                border-top:1px solid #eeeeee;">
                        <p style="color:#aaaaaa; font-size:12px; margin:0;">
                                                © 2026 Smart Appointment. All rights reserved.
                        </p>
                        </td>
                        </tr>
                
                        </table>
                        </td>
                        </tr>
                        </table>
                        </body>
                        </html>
                """.formatted(resetUrl);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Password Reset Request");
            helper.setText(mailBody, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            redisTemplate.delete(buildPasswordResetTokenKey(token));
            throw new MailSendException(email, e);
        }
    }
}

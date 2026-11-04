package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.dto.request.PasswordResetRequestDTO;
import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.exception.MailSendException;
import com.ogulcanonder.smart_appointment.service.impl.PasswordResetServiceImpl;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;

public class PasswordResetServiceImplTest {
    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private AccountService accountService;
    private JavaMailSender javaMailSender;
    private PasswordResetServiceImpl passwordResetServiceImpl;

    @Value("${password.reset.url}")
    private String passwordResetUrl;

    @BeforeEach
    public void setUp() {
        redisTemplate = Mockito.mock(RedisTemplate.class);
        valueOperations = Mockito.mock(ValueOperations.class);
        accountService = Mockito.mock(AccountService.class);
        javaMailSender = Mockito.mock(JavaMailSender.class);
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        passwordResetServiceImpl = new PasswordResetServiceImpl(redisTemplate, accountService,
                javaMailSender, passwordResetUrl);
    }

    @Test
    public void processRequest_shouldProcessRequestSuccessfully() {
        String testEmail = "test@gmail.com";
        Mockito.when(accountService.findByEmail(testEmail)).thenReturn(new Account());
        Mockito.when(valueOperations.get("password_reset_email:" + testEmail)).thenReturn(null);

        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        passwordResetServiceImpl.processRequest(testEmail);
        Mockito.verify(valueOperations, Mockito.times(1)).set(startsWith("password_reset_token:"), eq(testEmail), any(Duration.class));
        Mockito.verify(valueOperations, Mockito.times(1)).set(startsWith("password_reset_email:"), anyString(), any(Duration.class));
        Mockito.verify(javaMailSender).send(mimeMessage);
    }

    @Test
    public void processRequest_shouldAccountEmpty() {
        String testEmail = "test@gmail.com";
        Mockito.when(accountService.findByEmail(testEmail)).thenReturn(null);
        passwordResetServiceImpl.processRequest(testEmail);
        Mockito.verifyNoInteractions(redisTemplate, javaMailSender);
    }

    @Test
    public void processRequest_shouldPasswordResetTokenNotExist() {
        String testEmail = "test@gmail.com";
        Mockito.when(accountService.findByEmail(testEmail)).thenReturn(new Account());
        Mockito.when(valueOperations.get("password_reset_email:" + testEmail)).thenReturn(null);

        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        passwordResetServiceImpl.processRequest(testEmail);

        Mockito.verify(valueOperations, Mockito.times(1)).set(startsWith("password_reset_token:"), eq(testEmail), any(Duration.class));
        Mockito.verify(valueOperations, Mockito.times(1)).set(startsWith("password_reset_email:"), anyString(), any(Duration.class));
        Mockito.verify(javaMailSender).send(mimeMessage);
    }

    @Test
    public void processRequest_shouldPasswordResetTokenExist() {
        String testEmail = "test@gmail.com";
        String existingToken = "existingToken";
        Mockito.when(accountService.findByEmail(testEmail)).thenReturn(new Account());
        Mockito.when(valueOperations.get("password_reset_email:" + testEmail)).thenReturn(existingToken);

        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        passwordResetServiceImpl.processRequest(testEmail);

        Mockito.verify(redisTemplate, Mockito.times(1)).delete("password_reset_token:" + existingToken);
        Mockito.verify(valueOperations, Mockito.times(1)).set(startsWith("password_reset_token:"), eq(testEmail), any(Duration.class));
        Mockito.verify(valueOperations, Mockito.times(1)).set(startsWith("password_reset_email:"), anyString(), any(Duration.class));
        Mockito.verify(javaMailSender).send(mimeMessage);
    }

    @Test
    public void processRequest_shouldThrowException_whenFailedSendEmail() {
        String testEmail = "test@gmail.com";
        Mockito.when(accountService.findByEmail(testEmail)).thenReturn(new Account());
        Mockito.when(valueOperations.get("password_reset_email:" + testEmail)).thenReturn(null);

        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        Mockito.doThrow(new MailSendException(testEmail, new Throwable())).when(javaMailSender).send(mimeMessage);

        assertThrows(MailSendException.class, () -> passwordResetServiceImpl.processRequest(testEmail));
        Mockito.verify(redisTemplate, Mockito.times(1)).delete(anyString());
    }

    @Test
    public void resetPassword_shouldResetPasswordSuccessfully() {
        PasswordResetRequestDTO passwordResetRequestDTO= new PasswordResetRequestDTO("test-token","TestNewPassword123*");
        String testEmail = "test@gmail.com";
        Mockito.when(valueOperations.get("password_reset_token:" + passwordResetRequestDTO.token())).thenReturn(testEmail);
        Mockito.when(valueOperations.get("password_reset_email:" + testEmail)).thenReturn(null);

        Account account = new Account();
        Mockito.when(accountService.findByEmail(testEmail)).thenReturn(account);

        passwordResetServiceImpl.resetPassword(passwordResetRequestDTO);

        Mockito.verify(accountService, Mockito.times(1)).resetPassword(account, passwordResetRequestDTO);
        Mockito.verify(redisTemplate, Mockito.times(1)).delete("password_reset_token:" + passwordResetRequestDTO.token());
        Mockito.verify(redisTemplate, Mockito.times(1)).delete("password_reset_email:" + testEmail);
    }
}

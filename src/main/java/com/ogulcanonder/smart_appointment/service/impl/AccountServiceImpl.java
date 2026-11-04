package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.dto.request.PasswordResetRequestDTO;
import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.exception.ResourceNotFoundException;
import com.ogulcanonder.smart_appointment.repository.AccountRepository;
import com.ogulcanonder.smart_appointment.service.AccountService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void create(Account account) {
        if (accountRepository.existsByEmailOrPhone(account.getEmail(), account.getPhone())) {
            throw new DataIntegrityViolationException("Email or Phone already exists");
        }
        accountRepository.save(account);
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    @Transactional
    @Override
    public void resetPassword(Account account, PasswordResetRequestDTO passwordResetRequestDTO) {
        account.setPassword(passwordEncoder.encode(passwordResetRequestDTO.newPassword()));
        accountRepository.save(account);
    }
}

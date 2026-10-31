package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.exception.ResourceNotFoundException;
import com.ogulcanonder.smart_appointment.repository.AccountRepository;
import com.ogulcanonder.smart_appointment.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void create(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }
}

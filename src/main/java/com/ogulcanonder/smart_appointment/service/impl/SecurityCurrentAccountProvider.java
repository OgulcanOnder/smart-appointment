package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.service.CurrentAccountProvider;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityCurrentAccountProvider implements CurrentAccountProvider {
    @Override
    public Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Account account)) {
            throw new AuthenticationCredentialsNotFoundException("Account is not authenticated");
        }
        return account;
    }
}

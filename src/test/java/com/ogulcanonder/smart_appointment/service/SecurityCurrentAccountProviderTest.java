package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.service.impl.SecurityCurrentAccountProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SecurityCurrentAccountProviderTest {

    private SecurityCurrentAccountProvider securityCurrentAccountProvider;

    @BeforeEach
    public void setup() {
        securityCurrentAccountProvider = new SecurityCurrentAccountProvider();
    }

    @Test
    public void getCurrentAccount_shouldReturnAccount() {
        Account account = Mockito.mock(Account.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(account, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Account result = securityCurrentAccountProvider.getCurrentAccount();
        assertSame(account, result);
    }

    @Test
    public void getCurrentAccount_shouldThrowException_whenAccountIsNull() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> securityCurrentAccountProvider.getCurrentAccount());
    }

    @Test
    public void getCurrentAccount_shouldThrowException_WhenAccountIsNot(){
        Authentication authentication = new UsernamePasswordAuthenticationToken("test", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> securityCurrentAccountProvider.getCurrentAccount());
    }
}

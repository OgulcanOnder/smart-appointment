package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.enums.AccountType;
import com.ogulcanonder.smart_appointment.enums.Role;
import com.ogulcanonder.smart_appointment.repository.AccountRepository;
import com.ogulcanonder.smart_appointment.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

public class AccountServiceImplTest {

    private AccountServiceImpl accountServiceImpl;
    private AccountRepository accountRepository;


    @BeforeEach
    public void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        accountServiceImpl = new AccountServiceImpl(accountRepository);
    }

    @Test
    public void create_shouldSaveAccount() {
        Account account = Account.builder()
                .email("testaccount@gmail.com")
                .password("TestPassword")
                .phone("5555555555")
                .accountType(AccountType.USER)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_USER))
                .build();
        account.setId(1L);
        accountServiceImpl.create(account);
        Mockito.verify(accountRepository).save(account);
    }
}

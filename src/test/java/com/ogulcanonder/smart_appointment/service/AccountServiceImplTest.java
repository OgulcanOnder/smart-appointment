package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.dto.request.PasswordResetRequestDTO;
import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.enums.AccountType;
import com.ogulcanonder.smart_appointment.enums.Role;
import com.ogulcanonder.smart_appointment.repository.AccountRepository;
import com.ogulcanonder.smart_appointment.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountServiceImplTest {

    private AccountServiceImpl accountServiceImpl;
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    public void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        accountServiceImpl = new AccountServiceImpl(accountRepository,passwordEncoder);
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

    @Test
    public void resetPassword_ShouldResetPasswordSuccessfully() {
        Account account=Account.builder()
                .email("testaccount@gmail.com")
                .password(passwordEncoder.encode("TestPassword123*"))
                .phone("5555555555")
                .accountType(AccountType.USER)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_USER))
                .build();
        account.setId(1L);

        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO(
                "Test Token", "TestNewPassword123*");
        Mockito.when(passwordEncoder.encode("TestNewPassword123*")).thenReturn("EncodeTestNewPassword123*");
        account.setPassword(passwordEncoder.encode(passwordResetRequestDTO.newPassword()));

        accountServiceImpl.resetPassword(account, passwordResetRequestDTO);
        assertThat(account.getPassword()).isEqualTo("EncodeTestNewPassword123*");
        Mockito.verify(accountRepository).save(account);
    }
}

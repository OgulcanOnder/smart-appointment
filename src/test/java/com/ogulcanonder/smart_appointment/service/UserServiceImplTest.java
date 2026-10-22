package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.entity.User;
import com.ogulcanonder.smart_appointment.repository.UserRepository;
import com.ogulcanonder.smart_appointment.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserServiceImplTest {
    private UserServiceImpl userServiceImpl;
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        userServiceImpl = new UserServiceImpl(userRepository);
    }

    @Test
    public void create_shouldSaveUser() {
        Account account = new Account();
        User user = new User();

        userServiceImpl.create(account, user);

        assertThat(user.getAccount()).isEqualTo(account);
        Mockito.verify(userRepository).save(user);
    }
}

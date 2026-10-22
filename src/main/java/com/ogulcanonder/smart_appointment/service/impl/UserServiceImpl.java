package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.entity.User;
import com.ogulcanonder.smart_appointment.repository.UserRepository;
import com.ogulcanonder.smart_appointment.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(Account account, User user) {
        user.setAccount(account);
        userRepository.save(user);
    }
}

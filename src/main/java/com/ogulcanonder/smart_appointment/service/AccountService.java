package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.dto.request.PasswordResetRequestDTO;
import com.ogulcanonder.smart_appointment.entity.Account;

public interface AccountService {

    public void create(Account account);

    public Account findByEmail(String email);

    public void resetPassword(Account account, PasswordResetRequestDTO passwordResetRequestDTO);
}

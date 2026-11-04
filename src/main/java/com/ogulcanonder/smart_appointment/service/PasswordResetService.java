package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.dto.request.PasswordResetRequestDTO;

public interface PasswordResetService {

    public void processRequest(String email);

    public void resetPassword(PasswordResetRequestDTO passwordResetRequestDTO);
}

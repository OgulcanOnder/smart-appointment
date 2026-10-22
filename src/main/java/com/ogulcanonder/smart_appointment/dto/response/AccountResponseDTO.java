package com.ogulcanonder.smart_appointment.dto.response;

import com.ogulcanonder.smart_appointment.enums.AccountType;

public record AccountResponseDTO(
        String email,
        String phone,
        AccountType accountType
) {
}

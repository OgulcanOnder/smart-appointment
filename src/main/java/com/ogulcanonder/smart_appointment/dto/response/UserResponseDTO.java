package com.ogulcanonder.smart_appointment.dto.response;

public record UserResponseDTO(
        String name,
        String surname,
        AccountResponseDTO account
) {
}

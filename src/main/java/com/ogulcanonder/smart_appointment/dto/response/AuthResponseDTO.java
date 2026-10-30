package com.ogulcanonder.smart_appointment.dto.response;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken
) {
}

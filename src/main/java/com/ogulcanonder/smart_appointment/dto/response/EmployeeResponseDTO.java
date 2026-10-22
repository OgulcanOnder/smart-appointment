package com.ogulcanonder.smart_appointment.dto.response;

public record EmployeeResponseDTO(
        Long id,
        String name,
        String surname,
        AccountResponseDTO account
) {
}

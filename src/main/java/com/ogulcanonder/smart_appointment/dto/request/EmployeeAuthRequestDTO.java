package com.ogulcanonder.smart_appointment.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record EmployeeAuthRequestDTO(

        @Valid @NotNull AccountRegisterRequestDTO accountRegisterRequestDTO,

        @Valid @NotNull EmployeeRegisterRequestDTO employeeRegisterRequestDTO
) {
}

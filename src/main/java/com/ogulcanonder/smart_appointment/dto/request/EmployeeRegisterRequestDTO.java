package com.ogulcanonder.smart_appointment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record EmployeeRegisterRequestDTO(

        @NotBlank(message = "Name cannot must be blank")
        @Size(min = 3, max = 50, message = "Name must be 3-50 characters long")
        String name,

        @NotBlank(message = "Surname cannot must be blank")
        @Size(min = 3, max = 50, message = "Surname must be 3-50 characters long")
        String surname,

        @NotNull(message = "Business Id cannot be null")
        @Positive(message = "Business Id cannot be negative")
        Long businessId
) {
}

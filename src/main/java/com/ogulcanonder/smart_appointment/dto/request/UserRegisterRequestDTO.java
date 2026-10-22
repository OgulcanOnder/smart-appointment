package com.ogulcanonder.smart_appointment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequestDTO(

        @NotBlank(message = "Name cannot must be blank")
        @Size(min = 3, max = 50, message = "Name must be 3-50 characters long")
        String name,

        @NotBlank(message = "Name cannot must be blank")
        @Size(min = 3, max = 50, message = "Name must be 3-50 characters long")
        String surname
) {
}

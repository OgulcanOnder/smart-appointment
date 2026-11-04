package com.ogulcanonder.smart_appointment.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgotPasswordRequestDTO(
        @NotBlank(message = "E-mail cannot must be blank")
        @Email(message = "Enter a valid e-mail address")
        @Size(min = 5, max = 254, message = "E-mail must be 5-254 characters long")
        String email
) {
}

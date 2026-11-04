package com.ogulcanonder.smart_appointment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordResetRequestDTO(
        @NotBlank(message = "Token cannot be blank")
        @Size(min = 43, max = 43, message = "Reset password token must be exactly 43 characters long")
        @Pattern(regexp = "^[A-Za-z0-9_\\-]+$", message = "Invalid token format")
        String token,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 255, message = "Password must be least 8 characters long")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*]).*$",
                message = "Password must contain at least one digit, lowercase, uppercase, and special character")
        String newPassword
) {
}

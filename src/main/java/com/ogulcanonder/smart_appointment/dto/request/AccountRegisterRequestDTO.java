package com.ogulcanonder.smart_appointment.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AccountRegisterRequestDTO(

        @NotBlank(message = "E-mail cannot must be blank")
        @Email(message = "Enter a valid e-mail address")
        @Size(min = 5, max = 254, message = "E-mail must be 5-254 characters long")
        String email,

        @NotBlank(message = "Password cannot must be blank")
        @Size(min = 8, max = 254, message = "Password must be least 8 characters long ")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*]).*$",
                message = "Password must contain at least one digit, lowercase, uppercase, and special character")
        String password,

        @NotBlank(message = "Phone cannot must be blank")
        @Size(min = 10, max = 10, message = "Please enter it without a leading 0")
        String phone
) {
}

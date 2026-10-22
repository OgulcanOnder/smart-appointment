package com.ogulcanonder.smart_appointment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record BusinessRegisterRequestDTO(

        @NotBlank(message = "Name cannot must be blank")
        @Size(min = 3, max = 50, message = "Name must be 3-50 characters long")
        String name,

        @NotBlank(message = "Address cannot must be blank")
        @Size(min = 5, max = 255, message = "Address must be 5-255 characters long")
        String address,

        @NotNull(message = "City Id cannot be null")
        @Positive(message = "City Id cannot be negative")
        int cityId,

        @NotNull(message = "District Id cannot be null")
        @Positive(message = "District Id cannot be negative")
        int districtId
) {
}

package com.ogulcanonder.smart_appointment.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record WorkingHourRequestDTO(
        @NotNull(message = "Start time cannot be null")
        LocalTime startTime,

        @NotNull(message = "End time cannot be null")
        LocalTime endTime,

        @NotNull(message = "Appointment duration minutes cannot be null")
        @Min(value = 1, message = "The appointment duration minutes must be greater than 1")
        @Max(value = 1440, message = "The appointment duration minutes very large")
        int appointmentDurationMinutes,

        @NotNull(message = "Working days cannot be null")
        @NotEmpty(message = "At least one working day must be selected")
        Set<DayOfWeek> workingDays
) {
}

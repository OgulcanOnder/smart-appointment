package com.ogulcanonder.smart_appointment.dto.response;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record WorkingHourResponseDTO(
        LocalTime startTime,
        LocalTime endTime,
        int appointmentDurationMinutes,
        Set<DayOfWeek> workingDays,
        EmployeeResponseDTO employee
) {
}

package com.ogulcanonder.smart_appointment.mapper;

import com.ogulcanonder.smart_appointment.dto.response.WorkingHourResponseDTO;
import com.ogulcanonder.smart_appointment.entity.WorkingHour;
import org.springframework.stereotype.Component;

@Component
public class WorkingHourMapper {
    private final EmployeeMapper employeeMapper;

    public WorkingHourMapper(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    public WorkingHourResponseDTO toResponseDto(WorkingHour workingHour) {
        if (workingHour == null) {
            return null;
        }
        return new WorkingHourResponseDTO(
                workingHour.getStartTime(),
                workingHour.getEndTime(),
                workingHour.getAppointmentDurationMinutes(),
                workingHour.getWorkingDays(),
                employeeMapper.toResponseDto(workingHour.getEmployee())
        );
    }
}

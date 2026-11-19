package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.dto.request.WorkingHourRequestDTO;
import com.ogulcanonder.smart_appointment.dto.response.WorkingHourResponseDTO;

public interface WorkingHourService {
    public WorkingHourResponseDTO add(WorkingHourRequestDTO workingHourRequestDTO);
    public WorkingHourResponseDTO findByEmployeeId(Long employeeId);
    public void updateById(Long id, WorkingHourRequestDTO workingHourRequestDTO);
}

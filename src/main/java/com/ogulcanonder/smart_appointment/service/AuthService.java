package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.dto.request.BusinessAuthRequestDTO;
import com.ogulcanonder.smart_appointment.dto.request.EmployeeAuthRequestDTO;
import com.ogulcanonder.smart_appointment.dto.request.UserAuthRequestDTO;
import com.ogulcanonder.smart_appointment.dto.response.BusinessResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.EmployeeResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.UserResponseDTO;

public interface AuthService {
    public BusinessResponseDTO businessRegister(BusinessAuthRequestDTO businessAuthRequestDTO);

    public UserResponseDTO userRegister(UserAuthRequestDTO userAuthRequestDTO);

    public EmployeeResponseDTO employeeRegister(EmployeeAuthRequestDTO employeeAuthRequestDTO);
}

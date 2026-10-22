package com.ogulcanonder.smart_appointment.dto.response;

import java.util.List;


public record BusinessResponseDTO(
        String name,
        String address,
        String cityName,
        String districtName,
        AccountResponseDTO accountResponseDTO,
        List<EmployeeResponseDTO> employees
) {
}

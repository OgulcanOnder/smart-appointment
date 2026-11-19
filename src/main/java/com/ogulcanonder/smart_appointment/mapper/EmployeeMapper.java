package com.ogulcanonder.smart_appointment.mapper;

import com.ogulcanonder.smart_appointment.dto.response.EmployeeResponseDTO;
import com.ogulcanonder.smart_appointment.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    private final AccountMapper accountMapper;

    public EmployeeMapper(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    public EmployeeResponseDTO toResponseDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getSurname(),
                accountMapper.toResponseDto(employee.getAccount())
        );
    }
}

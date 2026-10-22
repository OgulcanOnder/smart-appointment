package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.entity.Employee;
import com.ogulcanonder.smart_appointment.repository.EmployeeRepository;
import com.ogulcanonder.smart_appointment.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void create(Account account, Employee employee) {
        employee.setAccount(account);
        employeeRepository.save(employee);
    }
}

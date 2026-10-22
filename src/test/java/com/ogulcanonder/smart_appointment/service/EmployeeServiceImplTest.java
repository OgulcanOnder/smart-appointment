package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.entity.Employee;
import com.ogulcanonder.smart_appointment.repository.EmployeeRepository;
import com.ogulcanonder.smart_appointment.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EmployeeServiceImplTest {
    private EmployeeServiceImpl employeeServiceImpl;
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() {
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeServiceImpl = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    public void create_shouldSaveEmployee() {
        Account account = new Account();
        Employee employee = new Employee();

        employeeServiceImpl.create(account, employee);

        assertThat(employee.getAccount()).isEqualTo(account);
        Mockito.verify(employeeRepository).save(employee);
    }
}

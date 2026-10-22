package com.ogulcanonder.smart_appointment.repository;

import com.ogulcanonder.smart_appointment.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

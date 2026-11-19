package com.ogulcanonder.smart_appointment.repository;

import com.ogulcanonder.smart_appointment.entity.WorkingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkingHourRepository extends JpaRepository<WorkingHour, Long> {
    Optional<WorkingHour> findByEmployeeId(Long employeeId);

    Optional<WorkingHour> findByIdAndEmployeeId(Long id, Long employeeId);
}

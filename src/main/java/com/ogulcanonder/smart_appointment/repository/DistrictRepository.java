package com.ogulcanonder.smart_appointment.repository;

import com.ogulcanonder.smart_appointment.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
}

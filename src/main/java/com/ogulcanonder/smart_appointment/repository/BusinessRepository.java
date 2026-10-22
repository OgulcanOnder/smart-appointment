package com.ogulcanonder.smart_appointment.repository;

import com.ogulcanonder.smart_appointment.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
}

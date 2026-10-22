package com.ogulcanonder.smart_appointment.repository;

import com.ogulcanonder.smart_appointment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

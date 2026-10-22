package com.ogulcanonder.smart_appointment.controller;

import com.ogulcanonder.smart_appointment.dto.request.BusinessAuthRequestDTO;
import com.ogulcanonder.smart_appointment.dto.request.UserAuthRequestDTO;
import com.ogulcanonder.smart_appointment.dto.request.EmployeeAuthRequestDTO;
import com.ogulcanonder.smart_appointment.dto.response.BusinessResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.EmployeeResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.UserResponseDTO;
import com.ogulcanonder.smart_appointment.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/businesses")
    public ResponseEntity<BusinessResponseDTO> registerBusiness(
            @Valid @RequestBody BusinessAuthRequestDTO businessAuthRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.businessRegister(businessAuthRequestDTO));
    }

    @PostMapping("/register/users")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserAuthRequestDTO userAuthRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.userRegister(userAuthRequestDTO));
    }

    @PostMapping("/register/employees")
    public ResponseEntity<EmployeeResponseDTO> registerEmployee(
            @Valid @RequestBody EmployeeAuthRequestDTO employeeAuthRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.employeeRegister(employeeAuthRequestDTO));
    }
}

package com.ogulcanonder.smart_appointment.controller;

import com.ogulcanonder.smart_appointment.dto.request.BusinessAuthRequestDTO;
import com.ogulcanonder.smart_appointment.dto.request.UserAuthRequestDTO;
import com.ogulcanonder.smart_appointment.dto.request.EmployeeAuthRequestDTO;
import com.ogulcanonder.smart_appointment.dto.request.LoginRequestDTO;
import com.ogulcanonder.smart_appointment.dto.response.BusinessResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.EmployeeResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.UserResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.AccessTokenResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.AuthResponseDTO;
import com.ogulcanonder.smart_appointment.service.AuthService;
import com.ogulcanonder.smart_appointment.service.CookieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final CookieService cookieService;

    public AuthController(AuthService authService, CookieService cookieService) {
        this.authService = authService;
        this.cookieService = cookieService;
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

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        AuthResponseDTO authResponseDTO = authService.login(loginRequestDTO);
        ResponseCookie responseCookie = cookieService.addRefreshTokenCookie(authResponseDTO.refreshToken());
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(new AccessTokenResponseDTO(authResponseDTO.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponseDTO> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        AuthResponseDTO authResponseDTO = authService.refreshToken(refreshToken);
        ResponseCookie responseCookie = cookieService.addRefreshTokenCookie(authResponseDTO.refreshToken());
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(new AccessTokenResponseDTO(authResponseDTO.accessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        ResponseCookie responseCookie = cookieService.removeRefreshTokenCookie();
        authService.logout(refreshToken);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).header(
                HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
    }
}

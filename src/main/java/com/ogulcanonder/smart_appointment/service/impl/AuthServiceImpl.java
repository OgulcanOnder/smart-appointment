package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.dto.request.BusinessAuthRequestDTO;
import com.ogulcanonder.smart_appointment.dto.request.EmployeeAuthRequestDTO;
import com.ogulcanonder.smart_appointment.dto.request.LoginRequestDTO;
import com.ogulcanonder.smart_appointment.dto.request.UserAuthRequestDTO;
import com.ogulcanonder.smart_appointment.dto.response.AccountResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.BusinessResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.EmployeeResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.UserResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.AuthResponseDTO;
import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.entity.City;
import com.ogulcanonder.smart_appointment.entity.District;
import com.ogulcanonder.smart_appointment.entity.Business;
import com.ogulcanonder.smart_appointment.entity.User;
import com.ogulcanonder.smart_appointment.entity.Employee;
import com.ogulcanonder.smart_appointment.enums.AccountType;
import com.ogulcanonder.smart_appointment.enums.Role;
import com.ogulcanonder.smart_appointment.service.AuthService;
import com.ogulcanonder.smart_appointment.service.AccountService;
import com.ogulcanonder.smart_appointment.service.BusinessService;
import com.ogulcanonder.smart_appointment.service.CityService;
import com.ogulcanonder.smart_appointment.service.DistrictService;
import com.ogulcanonder.smart_appointment.service.UserService;
import com.ogulcanonder.smart_appointment.service.EmployeeService;
import com.ogulcanonder.smart_appointment.service.JwtService;
import com.ogulcanonder.smart_appointment.service.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final AccountService accountService;
    private final BusinessService businessService;
    private final PasswordEncoder passwordEncoder;
    private final CityService cityService;
    private final DistrictService districtService;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;

    public AuthServiceImpl(AccountService accountService, BusinessService businessService,
                           PasswordEncoder passwordEncoder, CityService cityService, DistrictService districtService,
                           UserService userService, EmployeeService employeeService,
                           AuthenticationManager authenticationManager, JwtService jwtService,
                           RefreshTokenService refreshTokenService, UserDetailsService userDetailsService) {
        this.accountService = accountService;
        this.businessService = businessService;
        this.passwordEncoder = passwordEncoder;
        this.cityService = cityService;
        this.districtService = districtService;
        this.userService = userService;
        this.employeeService = employeeService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    @Override
    public BusinessResponseDTO businessRegister(BusinessAuthRequestDTO businessAuthRequestDTO) {
        Account account = Account.builder()
                .email(businessAuthRequestDTO.accountRegisterRequestDTO().email())
                .password(passwordEncoder.encode(businessAuthRequestDTO.accountRegisterRequestDTO().password()))
                .phone(businessAuthRequestDTO.accountRegisterRequestDTO().phone())
                .accountType(AccountType.BUSINESS)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_BUSINESS))
                .build();
        accountService.create(account);

        City city = cityService.getById(businessAuthRequestDTO.businessRegisterRequestDTO().cityId());
        District district = districtService.getById(businessAuthRequestDTO.businessRegisterRequestDTO().districtId());
        Business business = Business.builder()
                .name(businessAuthRequestDTO.businessRegisterRequestDTO().name())
                .address(businessAuthRequestDTO.businessRegisterRequestDTO().address())
                .city(city)
                .district(district)
                .build();
        businessService.create(account, business);
        return new BusinessResponseDTO(business.getName(), business.getAddress(), city.getName(), district.getName(),
                new AccountResponseDTO(business.getAccount().getEmail(), business.getAccount().getPhone(),
                        business.getAccount().getAccountType()), null);
    }

    @Transactional
    @Override
    public UserResponseDTO userRegister(UserAuthRequestDTO userAuthRequestDTO) {
        Account account = Account.builder()
                .email(userAuthRequestDTO.accountRegisterRequestDTO().email())
                .password(passwordEncoder.encode(userAuthRequestDTO.accountRegisterRequestDTO().password()))
                .phone(userAuthRequestDTO.accountRegisterRequestDTO().phone())
                .accountType(AccountType.USER)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_USER))
                .build();
        accountService.create(account);

        User user = User.builder()
                .name(userAuthRequestDTO.userRegisterRequestDTO().name())
                .surname(userAuthRequestDTO.userRegisterRequestDTO().surname())
                .build();
        userService.create(account, user);

        return new UserResponseDTO(user.getName(), user.getSurname(),
                new AccountResponseDTO(user.getAccount().getEmail(), user.getAccount().getPhone(),
                        user.getAccount().getAccountType()));
    }

    @Transactional
    @Override
    public EmployeeResponseDTO employeeRegister(EmployeeAuthRequestDTO employeeAuthRequestDTO) {
        Account account = Account.builder()
                .email(employeeAuthRequestDTO.accountRegisterRequestDTO().email())
                .password(passwordEncoder.encode(employeeAuthRequestDTO.accountRegisterRequestDTO().password()))
                .phone(employeeAuthRequestDTO.accountRegisterRequestDTO().phone())
                .accountType(AccountType.EMPLOYEE)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_EMPLOYEE))
                .build();
        accountService.create(account);

        Business business = businessService.getReferenceById(
                employeeAuthRequestDTO.employeeRegisterRequestDTO().businessId());
        Employee employee = Employee.builder()
                .name(employeeAuthRequestDTO.employeeRegisterRequestDTO().name())
                .surname(employeeAuthRequestDTO.employeeRegisterRequestDTO().surname())
                .business(business)
                .account(account)
                .build();
        employeeService.create(account, employee);
        return new EmployeeResponseDTO(employee.getId(), employee.getName(), employee.getSurname(),
                new AccountResponseDTO(employee.getAccount().getEmail(), employee.getAccount().getPhone(),
                        employee.getAccount().getAccountType()));
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDTO.email(), loginRequestDTO.password()));
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);
            String accountId = jwtService.extractAllClaims(refreshToken).get("accountId").toString();
            refreshTokenService.save(accountId, refreshToken);
            return new AuthResponseDTO(accessToken, refreshToken);
        }
        throw new BadCredentialsException("Invalid e-mail or password");
    }

    @Override
    public AuthResponseDTO refreshToken(String refreshToken) {
        String accountId = jwtService.extractAllClaims(refreshToken).get("accountId").toString();
        String email = jwtService.extractAllClaims(refreshToken).getSubject();
        if (refreshTokenService.isValid(accountId, refreshToken)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String newAccessToken = jwtService.generateAccessToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);
            refreshTokenService.save(accountId, newRefreshToken);
            return new AuthResponseDTO(newAccessToken, newRefreshToken);
        }
        throw new BadCredentialsException("Invalid refresh token");
    }

    @Override
    public void logout(String refreshToken) {
        String accountId = jwtService.extractAllClaims(refreshToken).get("accountId").toString();
        refreshTokenService.delete(accountId);
    }
}

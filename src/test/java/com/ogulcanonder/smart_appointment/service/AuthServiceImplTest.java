package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.dto.request.*;
import com.ogulcanonder.smart_appointment.dto.response.AccountResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.BusinessResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.EmployeeResponseDTO;
import com.ogulcanonder.smart_appointment.dto.response.UserResponseDTO;
import com.ogulcanonder.smart_appointment.entity.*;
import com.ogulcanonder.smart_appointment.enums.AccountType;
import com.ogulcanonder.smart_appointment.enums.Role;
import com.ogulcanonder.smart_appointment.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class AuthServiceImplTest {

    private AuthServiceImpl authServiceImpl;
    private AccountService accountService;
    private BusinessService businessService;
    private PasswordEncoder passwordEncoder;
    private CityService cityService;
    private DistrictService districtService;
    private UserService userService;
    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        accountService = Mockito.mock(AccountService.class);
        businessService = Mockito.mock(BusinessService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        cityService = Mockito.mock(CityService.class);
        districtService = Mockito.mock(DistrictService.class);
        userService = Mockito.mock(UserService.class);
        employeeService = Mockito.mock(EmployeeService.class);
        authServiceImpl = new AuthServiceImpl(accountService, businessService, passwordEncoder,
                cityService, districtService, userService, employeeService);
    }


    @Test
    public void businessRegister_shouldRegisterBusinessSuccessfully() {

        AccountRegisterRequestDTO accountRegisterRequestDTO = new AccountRegisterRequestDTO(
                "testbusiness@gmail.com", "TestPassword1903*", "5555555555");
        BusinessRegisterRequestDTO businessRegisterRequestDTO = new BusinessRegisterRequestDTO(
                "test business", "test address", 1, 1);
        BusinessAuthRequestDTO businessAuthRequestDTO = new BusinessAuthRequestDTO(
                accountRegisterRequestDTO, businessRegisterRequestDTO);

        City city = new City();
        city.setId(1);
        city.setName("testcity");
        Mockito.when(cityService.getById(1)).thenReturn(city);

        District district = new District();
        district.setId(1);
        district.setName("testdistrict");
        Mockito.when(districtService.getById(1)).thenReturn(district);

        Mockito.when(passwordEncoder.encode(any())).thenReturn("hashTestPassword");

        Mockito.doAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            account.setId(1L);
            account.setAccountType(AccountType.BUSINESS);
            account.setAuthorities(Set.of(Role.ROLE_BUSINESS));
            return null;
        }).when(accountService).create(Mockito.any(Account.class));

        Mockito.doAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            account.setId(1L);
            account.setAccountType(AccountType.BUSINESS);
            account.setAuthorities(Set.of(Role.ROLE_BUSINESS));
            Business business = invocation.getArgument(1);
            business.setId(1L);
            business.setAccount(account);
            return null;
        }).when(businessService).create(Mockito.any(Account.class), Mockito.any(Business.class));

        BusinessResponseDTO expectedResult = new BusinessResponseDTO(
                businessAuthRequestDTO.businessRegisterRequestDTO().name(),
                businessAuthRequestDTO.businessRegisterRequestDTO().address(), city.getName(), district.getName(),
                new AccountResponseDTO(businessAuthRequestDTO.accountRegisterRequestDTO().email(),
                        businessAuthRequestDTO.accountRegisterRequestDTO().phone(), AccountType.BUSINESS), null);

        BusinessResponseDTO result = authServiceImpl.businessRegister(businessAuthRequestDTO);

        assertEquals(expectedResult, result);

        Mockito.verify(accountService, Mockito.times(1)).create(any(Account.class));
        Mockito.verify(businessService, Mockito.times(1)).create(any(Account.class), any(Business.class));

    }

    @Test
    public void businessRegister_shouldThrowException_whenCityNotFound() {
        AccountRegisterRequestDTO accountRegisterRequestDTO = new AccountRegisterRequestDTO(
                "testbusiness@gmail.com", "TestPassword1903*", "5555555555");
        BusinessRegisterRequestDTO businessRegisterRequestDTO = new BusinessRegisterRequestDTO(
                "test business", "test address", 999, 1);
        BusinessAuthRequestDTO businessAuthRequestDTO = new BusinessAuthRequestDTO(
                accountRegisterRequestDTO, businessRegisterRequestDTO);

        Mockito.when(cityService.getById(999)).thenThrow(new RuntimeException("City not found"));
        Mockito.when(passwordEncoder.encode(any())).thenReturn("hashTestPassword");

        assertThrows(RuntimeException.class, () -> authServiceImpl.businessRegister(businessAuthRequestDTO));

        Mockito.verify(accountService, Mockito.times(1)).create(any(Account.class));
        Mockito.verify(businessService, Mockito.never()).create(any(Account.class), any(Business.class));
    }

    @Test
    public void businessRegister_shouldThrowException_whenDistrictNotFound() {
        AccountRegisterRequestDTO accountRegisterRequestDTO = new AccountRegisterRequestDTO(
                "testbusiness@gmail.com", "TestPassword1903*", "5555555555");
        BusinessRegisterRequestDTO businessRegisterRequestDTO = new BusinessRegisterRequestDTO(
                "test business", "test address", 1, 999);
        BusinessAuthRequestDTO businessAuthRequestDTO = new BusinessAuthRequestDTO(accountRegisterRequestDTO, businessRegisterRequestDTO);

        City city = new City();
        city.setId(1);
        city.setName("testcity");
        Mockito.when(cityService.getById(1)).thenReturn(city);

        Mockito.when(districtService.getById(999)).thenThrow(new RuntimeException("District not found"));

        Mockito.when(passwordEncoder.encode(any())).thenReturn("hashTestPassword");

        assertThrows(RuntimeException.class, () -> authServiceImpl.businessRegister(businessAuthRequestDTO));
        Mockito.verify(accountService, Mockito.times(1)).create(any(Account.class));
        Mockito.verify(businessService, Mockito.never()).create(any(Account.class), any(Business.class));

    }

    @Test
    public void businessRegister_shouldMapAccountAndBusinessCorrectly() {
        AccountRegisterRequestDTO accountRegisterRequestDTO = new AccountRegisterRequestDTO(
                "testbusiness@gmail.com", "TestPassword1903*", "5555555555");
        BusinessRegisterRequestDTO businessRegisterRequestDTO = new BusinessRegisterRequestDTO(
                "test business", "test address", 1, 1);
        BusinessAuthRequestDTO businessAuthRequestDTO = new BusinessAuthRequestDTO(accountRegisterRequestDTO, businessRegisterRequestDTO);

        City city = new City();
        city.setId(1);
        city.setName("testcity");

        District district = new District();
        district.setId(1);
        district.setName("testdistrict");

        Mockito.when(cityService.getById(1)).thenReturn(city);
        Mockito.when(districtService.getById(1)).thenReturn(district);
        Mockito.when(passwordEncoder.encode(accountRegisterRequestDTO.password())).thenReturn("hashTestPassword");

        Mockito.doAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            Business business = invocation.getArgument(1);
            business.setId(1L);
            business.setAccount(account);
            business.setCity(city);
            business.setDistrict(district);
            return null;
        }).when(businessService).create(Mockito.any(Account.class), Mockito.any(Business.class));

        authServiceImpl.businessRegister(businessAuthRequestDTO);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        ArgumentCaptor<Business> businessArgumentCaptor = ArgumentCaptor.forClass(Business.class);

        Mockito.verify(accountService).create(accountArgumentCaptor.capture());
        Mockito.verify(businessService).create(any(Account.class), businessArgumentCaptor.capture());

        Account capturedAccount = accountArgumentCaptor.getValue();
        assertEquals("testbusiness@gmail.com", capturedAccount.getEmail());
        assertEquals("hashTestPassword", capturedAccount.getPassword());
        assertEquals(AccountType.BUSINESS, capturedAccount.getAccountType());
        assertEquals(Set.of(Role.ROLE_BUSINESS), capturedAccount.getAuthorities());

        Business capturedBusiness = businessArgumentCaptor.getValue();
        assertEquals("test business", capturedBusiness.getName());
        assertEquals("test address", capturedBusiness.getAddress());
        assertEquals(1, capturedBusiness.getCity().getId());
        assertEquals(1, capturedBusiness.getDistrict().getId());

    }

    @Test
    public void userRegister_shouldRegisterUserSuccessfully() {
        AccountRegisterRequestDTO accountRegisterRequestDTO = new AccountRegisterRequestDTO(
                "testuser@gmail.com", "TestPassword1903*", "5555555555");
        UserRegisterRequestDTO userRegisterRequestDTO = new UserRegisterRequestDTO("test", "user");
        UserAuthRequestDTO userAuthRequestDTO = new UserAuthRequestDTO(accountRegisterRequestDTO, userRegisterRequestDTO);

        Mockito.doAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            account.setId(1L);
            account.setAccountType(AccountType.USER);
            account.setAuthorities(Set.of(Role.ROLE_USER));
            return null;
        }).when(accountService).create(Mockito.any(Account.class));

        Mockito.doAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            account.setId(1L);
            account.setAccountType(AccountType.USER);
            account.setAuthorities(Set.of(Role.ROLE_USER));
            User user = invocation.getArgument(1);
            user.setId(1L);
            user.setAccount(account);
            return null;
        }).when(userService).create(Mockito.any(Account.class), Mockito.any(User.class));

        Mockito.when(passwordEncoder.encode(accountRegisterRequestDTO.password())).thenReturn("hashTestPassword");

        UserResponseDTO expectedResult = new UserResponseDTO(userAuthRequestDTO.userRegisterRequestDTO().name(),
                userAuthRequestDTO.userRegisterRequestDTO().surname(),
                new AccountResponseDTO(userAuthRequestDTO.accountRegisterRequestDTO().email(),
                        userAuthRequestDTO.accountRegisterRequestDTO().phone(), AccountType.USER));

        UserResponseDTO result = authServiceImpl.userRegister(userAuthRequestDTO);

        assertEquals(expectedResult, result);

        Mockito.verify(accountService, Mockito.times(1)).create(Mockito.any(Account.class));
        Mockito.verify(userService, Mockito.times(1)).create(Mockito.any(Account.class), Mockito.any(User.class));
    }

    @Test
    public void userRegister_shouldMapAccountAndUserCorrectly() {
        AccountRegisterRequestDTO accountRegisterRequestDTO = new AccountRegisterRequestDTO(
                "testuser@gmail.com", "TestPassword1903*", "5555555555");
        UserRegisterRequestDTO userRegisterRequestDTO = new UserRegisterRequestDTO("test", "user");
        UserAuthRequestDTO userAuthRequestDTO = new UserAuthRequestDTO(accountRegisterRequestDTO, userRegisterRequestDTO);

        Mockito.doAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            User user = invocation.getArgument(1);
            user.setId(1L);
            user.setAccount(account);
            return null;
        }).when(userService).create(Mockito.any(Account.class), Mockito.any(User.class));

        Mockito.when(passwordEncoder.encode(accountRegisterRequestDTO.password())).thenReturn("hashTestPassword");

        authServiceImpl.userRegister(userAuthRequestDTO);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        Mockito.verify(accountService).create(accountArgumentCaptor.capture());
        Mockito.verify(userService).create(any(Account.class), userArgumentCaptor.capture());

        Account capturedAccount = accountArgumentCaptor.getValue();
        assertEquals("testuser@gmail.com", capturedAccount.getEmail());
        assertEquals("hashTestPassword", capturedAccount.getPassword());
        assertEquals(AccountType.USER, capturedAccount.getAccountType());
        assertEquals(Set.of(Role.ROLE_USER), capturedAccount.getAuthorities());

        User capturedUser = userArgumentCaptor.getValue();
        assertEquals("test", capturedUser.getName());
        assertEquals("user", capturedUser.getSurname());
    }

    @Test
    public void employeeRegister_shouldRegisterEmployeeSuccessfully() {
        AccountRegisterRequestDTO accountRegisterRequestDTO = new AccountRegisterRequestDTO(
                "testemployee@gmail.com", "TestPassword1903*", "5555555555");
        EmployeeRegisterRequestDTO employeeRegisterRequestDTO = new EmployeeRegisterRequestDTO(
                "test", "employee", 1L);
        EmployeeAuthRequestDTO employeeAuthRequestDTO =
                new EmployeeAuthRequestDTO(accountRegisterRequestDTO, employeeRegisterRequestDTO);

        City city = new City();
        city.setId(1);
        city.setName("testcity");
        Mockito.when(cityService.getById(1)).thenReturn(city);

        District district = new District();
        district.setId(1);
        district.setName("testdistrict");
        Mockito.when(districtService.getById(1)).thenReturn(district);

        Business business = new Business();
        business.setId(1L);
        business.setName("Test Business");
        business.setAddress("Test address");
        business.setCity(city);
        business.setDistrict(district);
        Mockito.when(businessService.getReferenceById(1L)).thenReturn(business);

        Mockito.doAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            account.setId(1L);
            account.setAccountType(AccountType.EMPLOYEE);
            account.setAuthorities(Set.of(Role.ROLE_EMPLOYEE));
            return null;
        }).when(accountService).create(Mockito.any(Account.class));

        Mockito.doAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            Employee employee = invocation.getArgument(1);
            employee.setId(1L);
            employee.setAccount(account);
            return null;
        }).when(employeeService).create(Mockito.any(Account.class), Mockito.any(Employee.class));

        EmployeeResponseDTO expectedResult = new EmployeeResponseDTO(
                1L, employeeAuthRequestDTO.employeeRegisterRequestDTO().name(), employeeAuthRequestDTO.employeeRegisterRequestDTO().surname(),
                new AccountResponseDTO(employeeAuthRequestDTO.accountRegisterRequestDTO().email(),
                        employeeAuthRequestDTO.accountRegisterRequestDTO().phone(), AccountType.EMPLOYEE));

        EmployeeResponseDTO result = authServiceImpl.employeeRegister(employeeAuthRequestDTO);

        assertEquals(expectedResult, result);

        Mockito.verify(accountService, Mockito.times(1)).create(Mockito.any(Account.class));
        Mockito.verify(employeeService, Mockito.times(1)).create(Mockito.any(Account.class), Mockito.any(Employee.class));

    }

    @Test
    public void employeeRegister_shouldMapAccountAndEmployeeCorrectly() {
        AccountRegisterRequestDTO accountRegisterRequestDTO = new AccountRegisterRequestDTO(
                "testemployee@gmail.com", "TestPassword1903*", "5555555555");
        EmployeeRegisterRequestDTO employeeRegisterRequestDTO = new EmployeeRegisterRequestDTO(
                "test", "employee", 1L);
        EmployeeAuthRequestDTO employeeAuthRequestDTO =
                new EmployeeAuthRequestDTO(accountRegisterRequestDTO, employeeRegisterRequestDTO);

        City city = new City();
        city.setId(1);
        city.setName("testcity");
        Mockito.when(cityService.getById(1)).thenReturn(city);

        District district = new District();
        district.setId(1);
        district.setName("testdistrict");
        Mockito.when(districtService.getById(1)).thenReturn(district);

        Business business = new Business();
        business.setId(1L);
        business.setName("Test Business");
        business.setAddress("Test address");
        business.setCity(city);
        business.setDistrict(district);
        Mockito.when(businessService.getReferenceById(1L)).thenReturn(business);

        Mockito.when(passwordEncoder.encode(accountRegisterRequestDTO.password())).thenReturn("hashTestPassword");

        Mockito.doAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            Employee employee = invocation.getArgument(1);
            employee.setId(1L);
            employee.setAccount(account);
            employee.setBusiness(business);
            return null;
        }).when(employeeService).create(Mockito.any(Account.class), Mockito.any(Employee.class));

        authServiceImpl.employeeRegister(employeeAuthRequestDTO);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);

        Mockito.verify(accountService).create(accountArgumentCaptor.capture());
        Mockito.verify(employeeService).create(Mockito.any(Account.class), employeeArgumentCaptor.capture());

        Account capturedAccount = accountArgumentCaptor.getValue();
        assertEquals("testemployee@gmail.com", capturedAccount.getEmail());
        assertEquals("hashTestPassword", capturedAccount.getPassword());
        assertEquals("5555555555", capturedAccount.getPhone());

        Employee capturedEmployee = employeeArgumentCaptor.getValue();
        assertEquals("test", capturedEmployee.getName());
        assertEquals("employee", capturedEmployee.getSurname());
        assertEquals(business, capturedEmployee.getBusiness());
    }
}

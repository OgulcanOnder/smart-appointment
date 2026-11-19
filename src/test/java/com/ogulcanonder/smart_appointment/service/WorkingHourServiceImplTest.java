package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.dto.request.WorkingHourRequestDTO;
import com.ogulcanonder.smart_appointment.dto.response.WorkingHourResponseDTO;
import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.entity.Business;
import com.ogulcanonder.smart_appointment.entity.Employee;
import com.ogulcanonder.smart_appointment.entity.WorkingHour;
import com.ogulcanonder.smart_appointment.exception.ResourceNotFoundException;
import com.ogulcanonder.smart_appointment.mapper.WorkingHourMapper;
import com.ogulcanonder.smart_appointment.repository.WorkingHourRepository;
import com.ogulcanonder.smart_appointment.service.impl.WorkingHourServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WorkingHourServiceImplTest {
    private WorkingHourRepository workingHourRepository;
    private WorkingHourMapper workingHourMapper;
    private CurrentAccountProvider currentAccountProvider;
    private WorkingHourServiceImpl workingHourServiceImpl;
    private Account account;

    @BeforeEach
    public void setup() {
        workingHourRepository = Mockito.mock(WorkingHourRepository.class);
        workingHourMapper = Mockito.mock(WorkingHourMapper.class);
        currentAccountProvider = Mockito.mock(CurrentAccountProvider.class);
        account = Mockito.mock(Account.class);
        workingHourServiceImpl = new WorkingHourServiceImpl(workingHourRepository, workingHourMapper, currentAccountProvider);
    }


    @Test
    public void add_shouldEmployeeWorkingHoursSuccessfully() {
        WorkingHourRequestDTO workingHourRequestDTO = new WorkingHourRequestDTO(LocalTime.of(9, 0), LocalTime.of(18, 0),
                30, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
        Mockito.when(currentAccountProvider.getCurrentAccount()).thenReturn(account);
        WorkingHour workingHour = WorkingHour.builder()
                .startTime(workingHourRequestDTO.startTime())
                .endTime(workingHourRequestDTO.endTime())
                .appointmentDurationMinutes(workingHourRequestDTO.appointmentDurationMinutes())
                .employee(account.getEmployee())
                .workingDays(workingHourRequestDTO.workingDays())
                .build();

        WorkingHour save = workingHourRepository.save(workingHour);
        WorkingHourResponseDTO expectedResult = workingHourMapper.toResponseDto(save);
        WorkingHourResponseDTO result = workingHourServiceImpl.add(workingHourRequestDTO);

        assertEquals(expectedResult, result);
        Mockito.verify(currentAccountProvider, Mockito.times(1)).getCurrentAccount();
        Mockito.verify(workingHourRepository, Mockito.times(1)).save(workingHour);
    }

    @Test
    public void add_shouldThrowException_whenEmployeeWorkingHoursHaveAlreadyBeenEntered() {
        WorkingHourRequestDTO workingHourRequestDTO = new WorkingHourRequestDTO(LocalTime.of(9, 0), LocalTime.of(18, 0),
                30, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));

        Mockito.when(currentAccountProvider.getCurrentAccount()).thenReturn(account);
        Mockito.when(workingHourRepository.save(Mockito.any(WorkingHour.class))).thenThrow(new DataIntegrityViolationException("The working hours have already been entered"));

        assertThrows(DataIntegrityViolationException.class, () -> workingHourServiceImpl.add(workingHourRequestDTO));
        Mockito.verify(workingHourRepository, Mockito.times(1)).save(Mockito.any(WorkingHour.class));
    }

    @Test
    public void findByEmployeeId_shouldReturnEmployeeWorkingHoursSuccessfully() {
        Long employeeId = 1L;
        WorkingHour workingHour = new WorkingHour();
        WorkingHourResponseDTO expectedResult = Mockito.mock(WorkingHourResponseDTO.class);

        Mockito.when(currentAccountProvider.getCurrentAccount()).thenReturn(account);
        Mockito.when(workingHourRepository.findByEmployeeId(employeeId)).thenReturn(Optional.of(workingHour));
        Mockito.when(workingHourMapper.toResponseDto(workingHour)).thenReturn(expectedResult);

        WorkingHourResponseDTO result = workingHourServiceImpl.findByEmployeeId(employeeId);

        assertEquals(expectedResult, result);
        Mockito.verify(currentAccountProvider, Mockito.times(1)).getCurrentAccount();
        Mockito.verify(workingHourRepository, Mockito.times(1)).findByEmployeeId(employeeId);
        Mockito.verify(workingHourMapper, Mockito.times(1)).toResponseDto(workingHour);
    }

    @Test
    public void findByEmployeeId_shouldThrowException_whenNotFoundEmployeeWorkingHours() {
        Long employeeId = 1L;
        Mockito.when(currentAccountProvider.getCurrentAccount()).thenReturn(account);
        Mockito.when(workingHourRepository.findByEmployeeId(employeeId)).thenThrow(new ResourceNotFoundException("Employee not found working hours"));
        Mockito.when(workingHourMapper.toResponseDto(Mockito.any(WorkingHour.class))).thenReturn(Mockito.mock(WorkingHourResponseDTO.class));

        assertThrows(ResourceNotFoundException.class, () -> workingHourServiceImpl.findByEmployeeId(employeeId));
        Mockito.verify(currentAccountProvider, Mockito.times(1)).getCurrentAccount();
        Mockito.verify(workingHourRepository, Mockito.times(1)).findByEmployeeId(employeeId);
        Mockito.verify(workingHourMapper, Mockito.times(0)).toResponseDto(Mockito.any(WorkingHour.class));
    }

    @Test
    public void updateById_shouldUpdateEmployeeWorkingHourSuccessfully() {
        Long id = 1L;
        WorkingHourRequestDTO workingHourRequestDTO = new WorkingHourRequestDTO(LocalTime.of(8, 0), LocalTime.of(17, 0),
                25, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY));
        Employee employee = new Employee(1L, "test", "test", Mockito.mock(Business.class), account);
        WorkingHour workingHour = WorkingHour.builder()
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(18, 0))
                .appointmentDurationMinutes(30)
                .employee(employee)
                // clear ve addAll işlemleri yaptığımız için new HashSet gerekli. Set.Of değiştirilemez olarak tutuyor nesneyi
                .workingDays(new HashSet<>(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)))
                .build();

        Mockito.when(currentAccountProvider.getCurrentAccount()).thenReturn(account);
        Mockito.when(account.getEmployee()).thenReturn(employee);
        Long employeeId = employee.getId();

        Mockito.when(workingHourRepository.findByIdAndEmployeeId(id, employeeId)).thenReturn(Optional.of(workingHour));

        workingHourServiceImpl.updateById(id, workingHourRequestDTO);

        assertEquals(workingHourRequestDTO.startTime(), workingHour.getStartTime());
        assertEquals(workingHourRequestDTO.endTime(), workingHour.getEndTime());
        assertEquals(workingHourRequestDTO.appointmentDurationMinutes(), workingHour.getAppointmentDurationMinutes());
        assertEquals(workingHourRequestDTO.workingDays(), workingHour.getWorkingDays());

        Mockito.verify(currentAccountProvider, Mockito.times(1)).getCurrentAccount();
        Mockito.verify(workingHourRepository, Mockito.times(1)).findByIdAndEmployeeId(id, employeeId);
        Mockito.verify(workingHourRepository, Mockito.times(1)).save(workingHour);
    }

    @Test
    public void updateById_shouldThrowException_whenNotFoundEmployeeWorkingHours() {
        Long id = 1L;
        WorkingHourRequestDTO workingHourRequestDTO = new WorkingHourRequestDTO(LocalTime.of(8, 0), LocalTime.of(17, 0),
                25, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY));
        Employee employee = new Employee(1L, "test", "test", Mockito.mock(Business.class), account);
        Mockito.when(currentAccountProvider.getCurrentAccount()).thenReturn(account);
        Mockito.when(account.getEmployee()).thenReturn(employee);
        Long employeeId = employee.getId();
        Mockito.when(workingHourRepository.findByIdAndEmployeeId(id, employeeId)).thenThrow(new ResourceNotFoundException("The employee has not working hours"));

        assertThrows(ResourceNotFoundException.class, ()-> workingHourServiceImpl.updateById(id, workingHourRequestDTO));

        Mockito.verify(currentAccountProvider, Mockito.times(1)).getCurrentAccount();
        Mockito.verify(workingHourRepository, Mockito.times(1)).findByIdAndEmployeeId(id, employeeId);
        Mockito.verify(workingHourRepository, Mockito.times(0)).save(Mockito.any(WorkingHour.class));
    }

}

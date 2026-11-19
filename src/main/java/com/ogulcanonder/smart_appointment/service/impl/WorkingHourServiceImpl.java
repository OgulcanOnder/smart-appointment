package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.dto.request.WorkingHourRequestDTO;
import com.ogulcanonder.smart_appointment.dto.response.WorkingHourResponseDTO;
import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.entity.WorkingHour;
import com.ogulcanonder.smart_appointment.exception.ResourceNotFoundException;
import com.ogulcanonder.smart_appointment.mapper.WorkingHourMapper;
import com.ogulcanonder.smart_appointment.repository.WorkingHourRepository;
import com.ogulcanonder.smart_appointment.service.CurrentAccountProvider;
import com.ogulcanonder.smart_appointment.service.WorkingHourService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WorkingHourServiceImpl implements WorkingHourService {

    private final WorkingHourRepository workingHourRepository;
    private final WorkingHourMapper workingHourMapper;
    private final CurrentAccountProvider currentAccountProvider;

    public WorkingHourServiceImpl(WorkingHourRepository workingHourRepository, WorkingHourMapper workingHourMapper,
                                  CurrentAccountProvider currentAccountProvider) {
        this.workingHourRepository = workingHourRepository;
        this.workingHourMapper = workingHourMapper;
        this.currentAccountProvider = currentAccountProvider;
    }

    @Transactional
    @Override
    public WorkingHourResponseDTO add(WorkingHourRequestDTO workingHourRequestDTO) {
        try {
            Account account = currentAccountProvider.getCurrentAccount();
            WorkingHour workingHour = WorkingHour.builder()
                    .startTime(workingHourRequestDTO.startTime())
                    .endTime(workingHourRequestDTO.endTime())
                    .appointmentDurationMinutes(workingHourRequestDTO.appointmentDurationMinutes())
                    .employee(account.getEmployee())
                    .workingDays(workingHourRequestDTO.workingDays())
                    .build();
            WorkingHour savedWorkingHour = workingHourRepository.save(workingHour);
            return workingHourMapper.toResponseDto(savedWorkingHour);
        } catch (DataIntegrityViolationException exception) {
            throw new DataIntegrityViolationException("The working hours have already been entered");
        }
    }

    @Override
    public WorkingHourResponseDTO findByEmployeeId(Long employeeId) {
        currentAccountProvider.getCurrentAccount();
        WorkingHour workingHour = workingHourRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("The employee not found working hours"));
        return workingHourMapper.toResponseDto(workingHour);
    }

    @Transactional
    @Override
    public void updateById(Long id, WorkingHourRequestDTO workingHourRequestDTO) {
        Long employeeId = currentAccountProvider.getCurrentAccount().getEmployee().getId();
        WorkingHour workingHour = workingHourRepository.findByIdAndEmployeeId(id, employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("The employee has not working hours"));
        workingHour.setStartTime(workingHourRequestDTO.startTime());
        workingHour.setEndTime(workingHourRequestDTO.endTime());
        workingHour.setAppointmentDurationMinutes(workingHourRequestDTO.appointmentDurationMinutes());
        // Silip Tekrardan Ekliyoruz ki Eski Verileri Getirme İhtimali Ortadan Kalksın
        workingHour.getWorkingDays().clear();
        workingHour.getWorkingDays().addAll(workingHourRequestDTO.workingDays());
        workingHourRepository.save(workingHour);
    }
}

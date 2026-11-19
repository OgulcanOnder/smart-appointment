package com.ogulcanonder.smart_appointment.controller;

import com.ogulcanonder.smart_appointment.dto.request.WorkingHourRequestDTO;
import com.ogulcanonder.smart_appointment.dto.response.WorkingHourResponseDTO;
import com.ogulcanonder.smart_appointment.service.WorkingHourService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/working-hours")
public class WorkingHourController {

    private final WorkingHourService workingHourService;

    public WorkingHourController(WorkingHourService workingHourService) {
        this.workingHourService = workingHourService;
    }

    @PostMapping
    public ResponseEntity<WorkingHourResponseDTO> addWorkingHour(
            @Valid @RequestBody WorkingHourRequestDTO workingHourRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(workingHourService.add(workingHourRequestDTO));
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<WorkingHourResponseDTO> getWorkingHour(@PathVariable Long employeeId) {
        return ResponseEntity.status(HttpStatus.OK).body(workingHourService.findByEmployeeId(employeeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void>updateWorkingHour(@PathVariable Long id,
                                                 @Valid @RequestBody WorkingHourRequestDTO workingHourRequestDTO) {
        workingHourService.updateById(id, workingHourRequestDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

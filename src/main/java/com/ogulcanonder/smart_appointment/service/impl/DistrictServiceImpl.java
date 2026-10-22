package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.entity.District;
import com.ogulcanonder.smart_appointment.repository.DistrictRepository;
import com.ogulcanonder.smart_appointment.service.DistrictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DistrictServiceImpl implements DistrictService {
    private final DistrictRepository districtRepository;

    public DistrictServiceImpl(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    @Override
    public District getById(int id) {
        return districtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("District not found"));
    }
}

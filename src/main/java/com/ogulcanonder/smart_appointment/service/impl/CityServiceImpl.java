package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.entity.City;
import com.ogulcanonder.smart_appointment.exception.ResourceNotFoundException;
import com.ogulcanonder.smart_appointment.repository.CityRepository;
import com.ogulcanonder.smart_appointment.service.CityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City getById(int id) {
        return cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City Not Found"));
    }
}

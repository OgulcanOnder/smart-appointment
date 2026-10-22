package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.entity.City;
import com.ogulcanonder.smart_appointment.repository.CityRepository;
import com.ogulcanonder.smart_appointment.service.impl.CityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CityServiceImplTest {

    private CityServiceImpl cityServiceImpl;
    private CityRepository cityRepository;

    @BeforeEach
    public void setup() {
        cityRepository = Mockito.mock(CityRepository.class);
        cityServiceImpl = new CityServiceImpl(cityRepository);
    }

    @Test
    public void getById_shouldReturnCity() {
        City city = new City();
        city.setId(1);
        city.setName("test city");

        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.of(city));
        City result = cityServiceImpl.getById(1);

        assertThat(result).isEqualTo(city);
        Mockito.verify(cityRepository).findById(1);
    }

    @Test
    public void getById_shouldThrowException_whenCityNotFound() {
        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cityServiceImpl.getById(1));
        Mockito.verify(cityRepository, Mockito.times(1)).findById(1);
    }
}

package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.entity.District;
import com.ogulcanonder.smart_appointment.repository.DistrictRepository;
import com.ogulcanonder.smart_appointment.service.impl.DistrictServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DistrictServiceImplTest {

    private DistrictServiceImpl districtServiceImpl;
    private DistrictRepository districtRepository;

    @BeforeEach
    public void setup() {
        districtRepository = Mockito.mock(DistrictRepository.class);
        districtServiceImpl = new DistrictServiceImpl(districtRepository);
    }

    @Test
    public void getById_shouldReturnDistrict() {
        District district = new District();
        district.setId(1);
        district.setName("test district");

        Mockito.when(districtRepository.findById(1)).thenReturn(Optional.of(district));
        District result = districtServiceImpl.getById(1);

        assertThat(result).isEqualTo(district);
        Mockito.verify(districtRepository).findById(1);
    }

    @Test
    public void getById_shouldThrowException_whenCityNotFound() {
        Mockito.when(districtRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> districtServiceImpl.getById(1));
        Mockito.verify(districtRepository, Mockito.times(1)).findById(1);
    }
}

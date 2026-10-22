package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.entity.Business;
import com.ogulcanonder.smart_appointment.repository.BusinessRepository;
import com.ogulcanonder.smart_appointment.service.impl.BusinessServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BusinessServiceImplTest {

    private BusinessServiceImpl businessServiceImpl;
    private BusinessRepository businessRepository;

    @BeforeEach
    public void setUp() {
        businessRepository = Mockito.mock(BusinessRepository.class);
        businessServiceImpl = new BusinessServiceImpl(businessRepository);
    }


    @Test
    public void create_shouldSaveBusiness() {
        Account account = new Account();
        Business business = new Business();

        businessServiceImpl.create(account, business);

        assertThat(business.getAccount()).isEqualTo(account);
        Mockito.verify(businessRepository).save(business);

    }

    @Test
    public void getReferenceById_shouldReturnBusiness() {
        Long id = 1L;
        Business expectedBusiness = new Business();
        Mockito.when(businessRepository.getReferenceById(id)).thenReturn(expectedBusiness);

        Business result = businessServiceImpl.getReferenceById(id);
        assertThat(result).isEqualTo(expectedBusiness);
        Mockito.verify(businessRepository).getReferenceById(id);

    }

}

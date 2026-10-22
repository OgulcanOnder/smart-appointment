package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.entity.Business;
import com.ogulcanonder.smart_appointment.repository.BusinessRepository;
import com.ogulcanonder.smart_appointment.service.BusinessService;
import org.springframework.stereotype.Service;

@Service
public class BusinessServiceImpl implements BusinessService {
    private final BusinessRepository businessRepository;

    public BusinessServiceImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public void create(Account account, Business business) {
        business.setAccount(account);
        businessRepository.save(business);
    }

    @Override
    public Business getReferenceById(Long id) {
        return businessRepository.getReferenceById(id);
    }
}

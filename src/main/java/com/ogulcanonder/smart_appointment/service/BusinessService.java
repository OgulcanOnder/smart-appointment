package com.ogulcanonder.smart_appointment.service;

import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.entity.Business;

public interface BusinessService {
    public void create(Account account, Business business);

    public Business getReferenceById(Long id);
}

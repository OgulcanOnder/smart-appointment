package com.ogulcanonder.smart_appointment.mapper;

import com.ogulcanonder.smart_appointment.dto.response.AccountResponseDTO;
import com.ogulcanonder.smart_appointment.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponseDTO toResponseDto(Account account) {
        if (account == null) {
            return null;
        }
        return new AccountResponseDTO(
                account.getEmail(),
                account.getPhone(),
                account.getAccountType()
        );
    }
}

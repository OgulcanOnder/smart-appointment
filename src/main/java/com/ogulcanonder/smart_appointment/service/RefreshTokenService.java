package com.ogulcanonder.smart_appointment.service;

import java.util.Optional;

public interface RefreshTokenService {

    public void save(String accountId, String refreshToken);

    public Optional<String>find(String accountId);

    public boolean isValid(String accountId, String refreshToken);

    public void delete (String accountId);
}

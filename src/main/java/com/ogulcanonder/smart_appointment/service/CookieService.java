package com.ogulcanonder.smart_appointment.service;

import org.springframework.http.ResponseCookie;

public interface CookieService {
    public ResponseCookie addRefreshTokenCookie(String refreshToken);

    public ResponseCookie removeRefreshTokenCookie();
}

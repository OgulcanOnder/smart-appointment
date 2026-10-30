package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.service.CookieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CookieServiceImpl implements CookieService {

    @Value("${jwt.refresh.token.expiration.millis}")
    private long refreshTokenCookieExpirationMillis;

    private static final String REFRESH_TOKEN_NAME = "refreshToken";

    @Override
    public ResponseCookie addRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/v1/auth")
                .maxAge(Duration.ofMillis(refreshTokenCookieExpirationMillis))
                .build();
    }

    @Override
    public ResponseCookie removeRefreshTokenCookie() {
        return ResponseCookie.from(REFRESH_TOKEN_NAME, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
    }
}

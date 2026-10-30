package com.ogulcanonder.smart_appointment.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    public String generateAccessToken(UserDetails userDetails);

    public Claims extractAllClaims(String token);

    public Boolean isTokenValid(String token, UserDetails userDetails);

    public String generateRefreshToken(UserDetails userDetails);

}

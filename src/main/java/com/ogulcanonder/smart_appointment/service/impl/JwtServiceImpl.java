package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.entity.Account;
import com.ogulcanonder.smart_appointment.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret.key}")
    private String JwtSecretKey;

    @Value("${jwt.access.token.expiration.millis}")
    private long accessTokenExpirationMillis;

    @Value("${jwt.refresh.token.expiration.millis}")
    private long refreshTokenExpirationMillis;

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        Account account = (Account) userDetails;
        Map<String, Object> claims = new HashMap<>();
        claims.put("accountId", account.getId());
        claims.put("name", resolveAccountName(account));
        claims.put("accountType", account.getAccountType());
        claims.put("roles", account.getAuthorities());
        return createToken(claims, account.getEmail(), accessTokenExpirationMillis);
    }

    private String createToken(Map<String, Object> claims, String email, Long expireTime) {
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = extractAllClaims(token);
        return userDetails.getUsername().equals(claims.getSubject()) && claims.getExpiration().after(new Date());
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("accountId", ((Account) userDetails).getId());
        claims.put("roles", userDetails.getAuthorities());
        claims.put("email", ((Account) userDetails).getEmail());
        return createToken(claims, userDetails.getUsername(), refreshTokenExpirationMillis);
    }


    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private String resolveAccountName(Account account) {
        return switch (account.getAccountType()) {
            case BUSINESS -> account.getBusiness().getName();
            case USER -> account.getUser().getName();
            case EMPLOYEE -> account.getEmployee().getName();
        };
    }
}

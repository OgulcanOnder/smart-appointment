package com.ogulcanonder.smart_appointment.service.impl;

import com.ogulcanonder.smart_appointment.exception.ResourceNotFoundException;
import com.ogulcanonder.smart_appointment.service.RefreshTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final long REFRESH_TOKEN_TTL_DAYS = 7;

    public RefreshTokenServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String buildKey(String accountId) {
        return "refresh_token:" + accountId;
    }

    @Override
    public void save(String accountId, String refreshToken) {
        redisTemplate.opsForValue().set(buildKey(accountId), refreshToken, Duration.ofDays(REFRESH_TOKEN_TTL_DAYS));

    }

    @Override
    public Optional<String> find(String accountId) {
        String token = redisTemplate.opsForValue().get(buildKey(accountId));
        return Optional.ofNullable(token);
    }

    @Override
    public boolean isValid(String accountId, String refreshToken) {
        return find(accountId)
                .map(stored -> stored.equals(refreshToken))
                .orElse(false);
    }

    @Override
    public void delete(String accountId) {
        find(accountId).orElseThrow(() -> new ResourceNotFoundException("Not Found Refresh Token"));
        redisTemplate.delete(buildKey(accountId));
    }
}

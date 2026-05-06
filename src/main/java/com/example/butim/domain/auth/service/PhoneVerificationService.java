package com.example.butim.domain.auth.service;

import com.example.butim.global.exception.CustomException;
import com.example.butim.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class PhoneVerificationService {

    private static final String CODE_PREFIX = "PHONE:";
    private static final String VERIFIED_PREFIX = "PHONE:VERIFIED:";
    private static final Duration CODE_TTL = Duration.ofMinutes(3);
    private static final Duration VERIFIED_TTL = Duration.ofMinutes(10);

    private final StringRedisTemplate redisTemplate;
    private final SecureRandom secureRandom = new SecureRandom();

    public String sendCode(String phoneNumber) {
        String code = String.format("%06d", secureRandom.nextInt(1_000_000));
        redisTemplate.opsForValue().set(CODE_PREFIX + phoneNumber, code, CODE_TTL);
        // SMS 연동 추후 예정 — 현재는 응답으로 반환
        return code;
    }

    public void verifyCode(String phoneNumber, String inputCode) {
        String savedCode = redisTemplate.opsForValue().get(CODE_PREFIX + phoneNumber);

        if (savedCode == null) {
            throw new CustomException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }
        if (!savedCode.equals(inputCode)) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        redisTemplate.delete(CODE_PREFIX + phoneNumber);
        redisTemplate.opsForValue().set(VERIFIED_PREFIX + phoneNumber, "true", VERIFIED_TTL);
    }

    public void checkVerified(String phoneNumber) {
        String verified = redisTemplate.opsForValue().get(VERIFIED_PREFIX + phoneNumber);
        if (!"true".equals(verified)) {
            throw new CustomException(ErrorCode.PHONE_NOT_VERIFIED);
        }
    }

    public void deleteVerified(String phoneNumber) {
        redisTemplate.delete(VERIFIED_PREFIX + phoneNumber);
    }
}
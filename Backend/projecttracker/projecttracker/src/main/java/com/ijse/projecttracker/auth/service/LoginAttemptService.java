package com.ijse.projecttracker.auth.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private final Cache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        this.attemptsCache = Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    public void loginSucceeded(String email) {
        attemptsCache.invalidate(email);
    }

    public void loginFailed(String email) {
        attemptsCache.put(email, getAttempts(email) + 1);
    }

    public int getAttempts(String email) {
        return Optional.ofNullable(attemptsCache.getIfPresent(email)).orElse(0);
    }

    public boolean isBlocked(String email) {
        return getAttempts(email) >= 5;
    }
}

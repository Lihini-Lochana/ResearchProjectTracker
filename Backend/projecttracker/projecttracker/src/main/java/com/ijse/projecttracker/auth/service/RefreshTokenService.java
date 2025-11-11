package com.ijse.projecttracker.auth.service;

import com.ijse.projecttracker.auth.entity.RefreshToken;
import com.ijse.projecttracker.auth.entity.User;
import com.ijse.projecttracker.auth.repository.RefreshTokenRepository;
import com.ijse.projecttracker.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository repo;
    private final UserRepository userRepo;
    private final long refreshExpirationMs;

    public RefreshTokenService(RefreshTokenRepository repo, UserRepository userRepo,
                               @Value("${app.jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(userRepo.getReferenceById(userId));
        token.setExpiresAt(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000));
        token.setCreatedAt(LocalDateTime.now());
        return repo.save(token);
    }

    public RefreshToken findByToken(String token) {
        return repo.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token not found"));
    }

    @Transactional
    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            repo.delete(token);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        repo.deleteByUserId(userId);
    }


}


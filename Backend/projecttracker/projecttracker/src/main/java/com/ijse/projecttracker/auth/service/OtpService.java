package com.ijse.projecttracker.auth.service;

import com.ijse.projecttracker.auth.entity.OtpPurpose;
import com.ijse.projecttracker.auth.entity.OtpToken;
import com.ijse.projecttracker.auth.entity.User;
import com.ijse.projecttracker.auth.repository.OtpRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class OtpService {

    private final OtpRepository otpRepository;
    private final EmailService emailService;

    public OtpService(OtpRepository otpRepository, EmailService emailService) {
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    public OtpToken generateOtpFor(User user, OtpPurpose purpose, int expiryMinutes) {
        String otp = String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000));

        OtpToken token = new OtpToken();
        token.setUser(user);
        token.setOtpCode(otp);
        token.setPurpose(purpose);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(expiryMinutes));
        token.setConsumed(false);

        OtpToken saved = otpRepository.save(token);

        emailService.sendOtpEmail(user.getEmail(), otp, purpose);
        return saved;
    }

    public OtpToken generatePasswordResetOtp(User user, int expiryMinutes) {
        return generateOtpFor(user, OtpPurpose.PASSWORD_RESET, expiryMinutes);
    }

    public OtpToken generateEmailVerificationOtp(User user, int expiryMinutes) {
        return generateOtpFor(user, OtpPurpose.EMAIL_VERIFY, expiryMinutes);
    }


    public OtpToken validateOtpAndGetToken(String otpCode, OtpPurpose purpose) {
        return otpRepository.findByOtpCodeAndPurposeAndConsumedFalse(otpCode, purpose)
                .map(token -> {
                    if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
                        throw new RuntimeException("OTP expired");
                    }
                    token.setConsumed(true);
                    return otpRepository.save(token);
                }).orElseThrow(() -> new RuntimeException("Invalid OTP"));
    }



}


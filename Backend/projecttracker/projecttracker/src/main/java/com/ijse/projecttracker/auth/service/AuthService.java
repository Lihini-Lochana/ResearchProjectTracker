package com.ijse.projecttracker.auth.service;

import com.ijse.projecttracker.auth.dto.AccountType;
import com.ijse.projecttracker.auth.dto.SignupRequest;
import com.ijse.projecttracker.auth.entity.*;
import com.ijse.projecttracker.auth.repository.OtpRepository;
import com.ijse.projecttracker.auth.repository.RoleRepository;
import com.ijse.projecttracker.auth.repository.UserRepository;
import com.ijse.projecttracker.projectmgt.entity.Batch;
import com.ijse.projecttracker.projectmgt.repository.BatchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;

    private final BatchRepository batchRepository;

    public AuthService(UserRepository userRepository, OtpRepository otpRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, OtpService otpService,
                       EmailService emailService, BatchRepository batchRepository) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.emailService = emailService;
        this.batchRepository = batchRepository;
    }

    @Transactional
    public User register(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail().toLowerCase()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");

        UserRoleName roleToAssign;
        if (req.getAccountType() == AccountType.ADMIN) {
            roleToAssign = UserRoleName.ROLE_ADMIN;
        } else if (req.getAccountType() == AccountType.PI) {
            roleToAssign = UserRoleName.ROLE_PI;
        } else {
            roleToAssign = UserRoleName.ROLE_MEMBER;
        }

        Role role = roleRepository.findByName(roleToAssign)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Role not configured"));

        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setStatus(UserStatus.PENDING);
        user.setEmailVerified(false);
        user.getRoles().add(role);
        user.setCreatedAt(LocalDateTime.now());

        if (req.getAccountType() == AccountType.MEMBER) {
            if (req.getBatchId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Batch is required for students");
            }
            Batch batch = batchRepository.findById(req.getBatchId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid batch"));
            user.setBatch(batch);
        }

        User saved = userRepository.save(user);

        var otpToken = otpService.generateOtpFor(saved, OtpPurpose.EMAIL_VERIFY, 15);
        emailService.sendOtpEmail(saved.getEmail(), otpToken.getOtpCode(), OtpPurpose.EMAIL_VERIFY);

        return saved;
    }

    @Transactional
    public void verifyEmail(String email, String otpCode) {
        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        OtpToken token = otpRepository.findByOtpCodeAndPurposeAndConsumedFalse(otpCode, OtpPurpose.EMAIL_VERIFY)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP"));

        if (!token.getUser().getId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP does not match user");

        if (token.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired");

        token.setConsumed(true);
        otpRepository.save(token);

        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

}

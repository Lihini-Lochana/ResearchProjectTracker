package com.ijse.projecttracker.auth.controller;

import com.ijse.projecttracker.auth.dto.*;
import com.ijse.projecttracker.auth.entity.OtpPurpose;
import com.ijse.projecttracker.auth.entity.User;
import com.ijse.projecttracker.auth.entity.UserRoleName;
import com.ijse.projecttracker.auth.entity.UserStatus;
import com.ijse.projecttracker.auth.repository.UserRepository;
import com.ijse.projecttracker.auth.security.JwtTokenProvider;
import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import com.ijse.projecttracker.auth.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final OtpService otpService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final EmailService emailService;


    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          RefreshTokenService refreshTokenService,
                          UserRepository userRepository,
                          OtpService otpService,
                          AuthService authService,
                          PasswordEncoder passwordEncoder,
                          LoginAttemptService loginAttemptService, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.otpService = otpService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest req) {
        User user = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "OTP sent to verify email"));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@Valid @RequestBody VerifyEmailRequest req) {
        User user = userRepository.findByEmail(req.getEmail().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean valid = otpService.validateOtpAndGetToken(req.getOtp(), OtpPurpose.EMAIL_VERIFY).isConsumed();
        if (!valid) throw new RuntimeException("Invalid or expired OTP");

        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req,  HttpServletRequest request) {
        String email = req.getEmail().toLowerCase();

        if (loginAttemptService.isBlocked(email)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many failed login attempts");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, req.getPassword())
            );

            loginAttemptService.loginSucceeded(email);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


            UserDetailsImpl userDetails = UserDetailsImpl.fromUser(user);
            String accessToken = jwtTokenProvider.generateAccessToken(userDetails);


            var refreshToken = refreshTokenService.createRefreshToken(user.getId());

            JwtResponse body = new JwtResponse(
                    accessToken,
                    refreshToken.getToken(),
                    "Bearer",
                    jwtTokenProvider.getJwtExpirationMs()
            );

            return ResponseEntity.ok(body);

        } catch (Exception ex) {
            loginAttemptService.loginFailed(email);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }



    @PostMapping("/login/recovery")
    public ResponseEntity<?> recoveryLogin(@Valid @RequestBody RecoveryLoginRequest req, HttpServletRequest request) {
        String email = req.getEmail().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean valid = jwtTokenProvider.validateRecoveryToken(req.getRecoveryToken(), email);
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid recovery token");
        }

        UserDetailsImpl userDetails = UserDetailsImpl.fromUser(user);
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);


        var refreshToken = refreshTokenService.createRefreshToken(user.getId());



        JwtResponse body = new JwtResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                jwtTokenProvider.getJwtExpirationMs()
        );


        return ResponseEntity.ok(body);
    }



    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(@Valid @RequestBody TokenRefreshRequest req) {
        var refresh = refreshTokenService.findByToken(req.getRefreshToken());
        refreshTokenService.verifyExpiration(refresh);

        User user = refresh.getUser();
        UserDetailsImpl ud = UserDetailsImpl.fromUser(user);
        String newAccessToken = jwtTokenProvider.generateAccessToken(ud);

        return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, refresh.getToken(),
                jwtTokenProvider.getJwtExpirationMs()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest req) {
        User user = userRepository.findByEmail(req.getEmail().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));

        otpService.generatePasswordResetOtp(user, 15);
        return ResponseEntity.ok("Password reset OTP sent to email");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest req) {
        User user = userRepository.findByEmail(req.getEmail().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        otpService.validateOtpAndGetToken(req.getOtp(), OtpPurpose.PASSWORD_RESET);



        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);

        refreshTokenService.deleteByUserId(user.getId());

        return ResponseEntity.ok("Password reset successfully");
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/supervisors")
    public ResponseEntity<List<Map<String, Object>>> getAllSupervisors(
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {

        System.out.println("Current admin: " + currentUser.getUsername());

        List<User> supervisors = userRepository.findByRoleName(UserRoleName.ROLE_SUPERVISOR);

        List<Map<String, Object>> response = supervisors.stream().map(u -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("fullName", u.getFullName());
            map.put("email", u.getEmail());
            return map;
        }).toList();

        System.out.println("Found supervisors: " + supervisors.size());
        return ResponseEntity.ok(response);
    }

}

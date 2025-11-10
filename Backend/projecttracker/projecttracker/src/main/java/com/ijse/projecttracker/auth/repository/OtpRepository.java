package com.ijse.projecttracker.auth.repository;

import com.ijse.projecttracker.auth.entity.OtpToken;
import com.ijse.projecttracker.auth.entity.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByOtpCodeAndPurposeAndConsumedFalse(String otpCode, OtpPurpose purpose);
}

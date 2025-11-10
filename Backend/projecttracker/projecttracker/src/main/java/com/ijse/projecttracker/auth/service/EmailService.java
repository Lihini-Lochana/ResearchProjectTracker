package com.ijse.projecttracker.auth.service;

import com.ijse.projecttracker.auth.entity.OtpPurpose;

public interface EmailService {
    void sendOtpEmail(String to, String otpCode, OtpPurpose purpose);


}

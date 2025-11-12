package com.ijse.projecttracker.auth.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryLoginRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String recoveryToken;
}


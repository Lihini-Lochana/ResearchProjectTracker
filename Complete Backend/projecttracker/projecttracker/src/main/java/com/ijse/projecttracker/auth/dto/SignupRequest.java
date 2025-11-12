package com.ijse.projecttracker.auth.dto;

import com.ijse.projecttracker.auth.validation.PasswordComplexity;
import com.ijse.projecttracker.auth.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class SignupRequest {
    @NotBlank private String fullName;
    @NotBlank @Email private String email;
    @NotBlank @PasswordComplexity(min = 8) private String password;
    @NotBlank private String confirmPassword;
    @NotNull private AccountType accountType;
    private Long batchId;



}

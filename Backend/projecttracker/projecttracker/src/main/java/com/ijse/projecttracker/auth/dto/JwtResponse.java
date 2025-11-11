package com.ijse.projecttracker.auth.dto;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long expiresIn;
}

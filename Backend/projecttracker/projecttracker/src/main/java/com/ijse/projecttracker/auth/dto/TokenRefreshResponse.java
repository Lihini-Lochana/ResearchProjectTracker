package com.ijse.projecttracker.auth.dto;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefreshResponse {

        private String accessToken;
        private String refreshToken;
        private long expiresIn;

}

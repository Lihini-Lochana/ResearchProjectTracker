package com.ijse.projecttracker.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.Date;

@Getter
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long jwtExpirationMs;

    public JwtTokenProvider(@Value("${app.jwt.secret}") String base64Secret,
                            @Value("${app.jwt.expiration-ms}") long jwtExpirationMs) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Secret));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateAccessToken(UserDetailsImpl userDetails) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim("roles", userDetails.getAuthorities().stream().map(Object::toString).toList())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String generateRecoveryToken(UserDetailsImpl userDetails, long expirationMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("recovery", true)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public boolean validateRecoveryToken(String token, String expectedEmail) {
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            Boolean isRecovery = claims.get("recovery", Boolean.class);
            String subject = claims.getSubject();
            return Boolean.TRUE.equals(isRecovery) && subject.equalsIgnoreCase(expectedEmail);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

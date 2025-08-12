package com.example.Freelance.Project.Bidding.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:36000000}") // Default: 10 hours (in milliseconds)
    private long expirationTime;

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Updated to accept role and add it as a claim
    public String generateToken(String email, String role) {
        logger.debug("Generating token for email: {}, role: {}", email, role);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            String username = getClaims(token).getSubject();
            logger.debug("Extracted username: {} from token", username);
            return username;
        } catch (Exception e) {
            logger.error("Failed to extract username from token: {}", e.getMessage());
            throw e;
        }
    }

    // Optional: Extract role from token (for future use in authorization)
    public String extractRole(String token) {
        try {
            return getClaims(token).get("role", String.class);
        } catch (Exception e) {
            logger.error("Failed to extract role from token: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = getClaims(token);
            String username = claims.getSubject();
            boolean isValid = username != null &&
                    username.equals(userDetails.getUsername()) &&
                    !isTokenExpired(claims);
            logger.debug("Token validation for user {}: {}", username, isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.warn("Token expired: {}", e.getMessage());
            throw new RuntimeException("Token expired", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported token: {}", e.getMessage());
            throw new RuntimeException("Unsupported token", e);
        } catch (MalformedJwtException e) {
            logger.error("Malformed token: {}", e.getMessage());
            throw new RuntimeException("Malformed token", e);
        } catch (SignatureException e) {
            logger.error("Invalid signature: {}", e.getMessage());
            throw new RuntimeException("Invalid signature", e);
        } catch (Exception e) {
            logger.error("Invalid token: {}", e.getMessage());
            throw new RuntimeException("Invalid token", e);
        }
    }

    private boolean isTokenExpired(Claims claims) {
        boolean expired = claims.getExpiration().before(new Date());
        if (expired) {
            logger.warn("Token expired at: {}", claims.getExpiration());
        }
        return expired;
    }
}
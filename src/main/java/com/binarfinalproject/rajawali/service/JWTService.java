package com.binarfinalproject.rajawali.service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JWTService {
    public String generateToken(UserDetails userDetails);

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    public boolean isTokenValid(String token, UserDetails userDetails);

    public boolean isTokenExpired(String token);

    public Date extractExpiration(String token);

    public String extractUsername(String token);

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    public Claims extractAllClaims(String token);

    public Key getSignInKey();
}

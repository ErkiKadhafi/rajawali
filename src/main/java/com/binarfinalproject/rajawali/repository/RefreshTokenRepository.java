package com.binarfinalproject.rajawali.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.binarfinalproject.rajawali.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    
    Optional<RefreshToken> findByUserId(UUID userId);
}

package com.example.freelance.repositories;

import com.example.freelance.entities.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUsername(String username);
    Optional<RefreshToken> findByUsername(String username);
    void delete(RefreshToken token);
    RefreshToken save(RefreshToken token);
}

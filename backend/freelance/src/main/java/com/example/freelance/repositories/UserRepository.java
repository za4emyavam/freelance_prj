package com.example.freelance.repositories;

import com.example.freelance.entities.User;

import java.util.Optional;

public interface UserRepository {
    boolean existsByEmail(String email);
    User save(User user);
    Optional<User> getByEmail(String email);
    User update(User user);
    void deactivateProfileById(Long userId);
    boolean isUserActive(String email);
}

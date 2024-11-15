package com.example.freelance.repositories;

import com.example.freelance.entities.User;
import com.example.freelance.entities.dto.CreateUserDTO;

import java.time.LocalDate;
import java.util.Optional;

public interface UserRepository {
    boolean existsByEmail(String email);
    User save(CreateUserDTO user);
    Optional<User> getByEmail(String email);
    User update(User user);
    void deactivateProfileById(Long userId);
    boolean isUserActive(String email);
    String getRole(String username);
    int countRegisterUsers(LocalDate from, LocalDate to);
    double getAverageRating();
}

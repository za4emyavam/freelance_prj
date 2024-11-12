package com.example.freelance.service;

import com.example.freelance.entities.User;
import com.example.freelance.entities.dto.UpdateUserDTO;
import com.example.freelance.entities.dto.UserInfoDTO;
import com.example.freelance.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

//TODO EXCEPTIONS

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getByEmail(String email) {
        return userRepository.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email address already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public UserInfoDTO update(UpdateUserDTO updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = getByEmail(((User) authentication.getPrincipal()).getUsername());

        if (updatedUser.getName() != null)
            user.setName(updatedUser.getName());

        if (updatedUser.getSecondName() != null)
            user.setSecondName(updatedUser.getSecondName());

        if (updatedUser.getSurname() != null)
            user.setSurname(updatedUser.getSurname());

        if (updatedUser.getBirthday() != null)
            user.setBirthday(updatedUser.getBirthday());

        if (updatedUser.getPhoneNum() != null)
            user.setPhoneNum(updatedUser.getPhoneNum());

        if (updatedUser.getAboutMe() != null)
            user.setAboutMe(updatedUser.getAboutMe());

        if (updatedUser.getGender() != null)
            user.setGender(updatedUser.getGender());

        return new UserInfoDTO(userRepository.update(user));
    }

    public UserInfoDTO getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return new UserInfoDTO(getByEmail(user.getEmail()));
    }

    public void deactivateProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        userRepository.deactivateProfileById(user.getIdUser());
    }

    public boolean isUserActive(String email) {
        return userRepository.isUserActive(email);
    }
}

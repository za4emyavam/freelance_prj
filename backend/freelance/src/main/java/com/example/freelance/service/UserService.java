package com.example.freelance.service;

import com.example.freelance.entities.User;
import com.example.freelance.entities.dto.CreateUserDTO;
import com.example.freelance.entities.dto.PeriodRequestDTO;
import com.example.freelance.entities.dto.UpdateUserDTO;
import com.example.freelance.entities.dto.UserInfoDTO;
import com.example.freelance.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

//TODO EXCEPTIONS

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByEmail(String email) {
        Optional<User> user = userRepository.getByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            if (userRepository.getRole(email).equals("admin"))
                return new User(0L, email, null, null, null, null, null, null, null, null, User.UserRole.ADMIN, null, User.UserStatus.ACTIVE);

            throw new RuntimeException("User not found");
        }
    }

    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
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

    public User createUser(CreateUserDTO createUserDTO) {
        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            throw new IllegalArgumentException("Email address already in use");
        }

        if (!createUserDTO.getRole().equals("CUSTOMER") && !createUserDTO.getRole().equals("CONTRACTOR")) {
            throw new IllegalArgumentException("Illegal role");
        }

        return userRepository.save(createUserDTO);
    }

    public int countRegisteredUsersForPeriod(PeriodRequestDTO period) {
        LocalDate dateTo = period.getTo().plusDays(1);
        return userRepository.countRegisterUsers(period.getFrom(), dateTo);
    }

    public double getAverageRating() {
        return userRepository.getAverageRating();
    }
}

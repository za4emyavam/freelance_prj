package com.example.freelance.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    private Long idUser;
    private String email;
    private String password;
    private String name;
    private String secondName;
    private String surname;
    private LocalDate birthday;
    private String gender;
    private String phoneNum;
    private String aboutMe;
    private UserRole role;
    private LocalDateTime registrationDate;
    private UserStatus status;

    public enum UserRole {
        CUSTOMER, CONTRACTOR, ADMIN
    }

    public enum UserStatus {
        ACTIVE, DEACTIVATED
    }

    @Override
    public boolean isEnabled() {
        return status.equals(UserStatus.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (this.role) {
            case ADMIN -> List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"));
            case CUSTOMER -> List.of(
                    new SimpleGrantedAuthority("ROLE_CUSTOMER"));
            case CONTRACTOR -> List.of(
                    new SimpleGrantedAuthority("ROLE_CONTRACTOR"));
        };
    }
}

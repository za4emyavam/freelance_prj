package com.example.freelance.conf;

import com.example.freelance.entities.RefreshToken;
import com.example.freelance.entities.dto.JwtAuthenticationResponse;
import com.example.freelance.entities.dto.LoginDTO;
import com.example.freelance.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserDataSourceService userDataSourceService;
    private final UserRoutingDataSource userRoutingDataSource;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService, UserDataSourceService userDataSourceService, UserRoutingDataSource userRoutingDataSource, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.userDataSourceService = userDataSourceService;
        this.userRoutingDataSource = userRoutingDataSource;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtAuthenticationResponse authenticate(LoginDTO loginDTO) {

        // Проверка логина и пароля с использованием connect_user
        if (userDataSourceService.checkCredentialsWithConnectUser(loginDTO.getUsername(), passwordEncoder.encode(loginDTO.getPassword()))) {
            DataSource userDataSource = userDataSourceService.createUserDataSource(loginDTO.getUsername(), passwordEncoder.encode(loginDTO.getPassword()));
            userRoutingDataSource.addUserDataSource(loginDTO.getUsername(), userDataSource);
        } else {
            throw new RuntimeException("Invalid credentials");
        }

        UserDetails user = userService.userDetailsService().loadUserByUsername(loginDTO.getUsername());

        return new JwtAuthenticationResponse(jwtService.generateToken(user), jwtService.getRefreshToken(user.getUsername()).getToken());
    }

    public Map<String, String> refreshToken(String refreshTokenStr) {
        return jwtService.findByToken(refreshTokenStr)
                .map(jwtService::verifyExpiration)
                .map(RefreshToken::getUsername)
                .map(username -> {
                    String newAccessToken = jwtService.generateToken(userService.userDetailsService().loadUserByUsername(username));
                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("accessToken", newAccessToken);
                    tokens.put("refreshToken", jwtService.getRefreshToken(username).getToken());
                    return tokens;
                })
                .orElseGet(() -> null);
    }

    @Transactional
    public void logout(String username) {
        jwtService.deleteByUsername(username);
    }
}

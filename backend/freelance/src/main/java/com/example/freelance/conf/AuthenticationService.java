package com.example.freelance.conf;

import com.example.freelance.entities.RefreshToken;
import com.example.freelance.entities.dto.JwtAuthenticationResponse;
import com.example.freelance.entities.dto.LoginDTO;
import com.example.freelance.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthenticationService(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public JwtAuthenticationResponse authenticate(LoginDTO loginDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword()));

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

package com.example.freelance.conf;

import com.example.freelance.entities.dto.JwtAuthenticationResponse;
import com.example.freelance.entities.dto.LoginDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1")
public class LogInRestController {
    private final AuthenticationService authenticationService;

    public LogInRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse signIn(@RequestBody LoginDTO loginDTO) {
        return authenticationService.authenticate(loginDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshTokenStr = request.get("refreshToken");
        Map<String, String> newTokens = authenticationService.refreshToken(refreshTokenStr);
        return newTokens == null ? new ResponseEntity<>(HttpStatus.FORBIDDEN) : new ResponseEntity<>(newTokens, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        authenticationService.logout(username);
        return ResponseEntity.ok("User logged out successfully.");
    }
}

package com.example.freelance.conf;

import com.example.freelance.entities.dto.CreateUserDTO;
import com.example.freelance.entities.dto.JwtAuthenticationResponse;
import com.example.freelance.entities.dto.LoginDTO;
import com.example.freelance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class LogInRestController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public LogInRestController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
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

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        return userService.createUser(createUserDTO) == null ? new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED) : ResponseEntity.ok("User created");
    }
}

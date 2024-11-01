package com.example.freelance.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    private Long tokenId;
    private String token;
    private String username;
    private LocalDateTime expiryDate;
}

package com.example.freelance.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemovingReason {
    private Long idReason;
    private Long idApprovedContractor;
    private String reason;
    private LocalDateTime recallDate;
}

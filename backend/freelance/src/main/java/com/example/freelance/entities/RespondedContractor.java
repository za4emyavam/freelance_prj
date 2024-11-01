package com.example.freelance.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespondedContractor {
    private Long idRespondedContractor;
    private Long idTask;
    private Long idContractor;
    private ContractorStatus status;
    private LocalDateTime feedbackDate;

    public enum ContractorStatus {
        AGREED, APPROVED, REJECTED
    }
}

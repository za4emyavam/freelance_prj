package com.example.freelance.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespondedContractorDTO {
    private Long userId;
    private String email;
    private String name;
    private String secondName;
    private String surname;
    private Double rating;
    private Integer taskCompleted;
    private ContractorStatus status;

    public enum ContractorStatus {
        AGREED, APPROVED, REJECTED
    }
}

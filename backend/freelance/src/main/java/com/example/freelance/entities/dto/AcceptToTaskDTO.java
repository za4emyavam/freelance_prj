package com.example.freelance.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptToTaskDTO {
    private Long contractorId;
    private Long taskId;
    private LocalDateTime acceptDate;
}

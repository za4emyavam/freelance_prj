package com.example.freelance.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveTaskDTO {
    private Long taskId;
    private String name;
    private String email;
    private LocalDateTime endDate;
    private LocalDateTime creationDate;
    private Long fieldId;
    private String field;
    private Double cost;
}

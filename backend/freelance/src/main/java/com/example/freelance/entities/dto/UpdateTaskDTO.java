package com.example.freelance.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskDTO {
    private String name;
    private String description;
    private LocalDateTime endDate;
    private Double cost;
    private Long idActivityField;
}

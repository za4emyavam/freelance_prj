package com.example.freelance.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskDTO {
    private Long idCustomer;
    private Long idActivityField;
    private String name;
    private LocalDateTime endDate;
    private Double cost;
    private String description;
    private List<String> links;
}

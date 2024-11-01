package com.example.freelance.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long idTask;
    private Long idCustomer;
    private Long idActivityField;
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime startDate;
    private LocalDateTime finishedDate;
    private LocalDateTime endDate;
    private Double cost;
    private String description;
    private Status status;
    private Integer taskRate;
    private List<TaskFile> links;

    public enum Status {
        ACTIVE, PERFORMED, DONE, CANCELLED
    }
}

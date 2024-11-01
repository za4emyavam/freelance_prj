package com.example.freelance.entities.dto;

import com.example.freelance.entities.Task;
import com.example.freelance.entities.TaskFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractorTaskDTO {
    private Long taskId;
    private String taskName;
    private String description;
    private String name;
    private String surname;
    private String secondName;
    private LocalDateTime endDate;
    private LocalDateTime creationDate;
    private Long fieldId;
    private String field;
    private Double cost;
    private String email;
    private String phoneNumber;
    private List<TaskFile> links;
    private Task.Status status;
}

package com.example.freelance.entities.dto;

import com.example.freelance.entities.FieldOfActivity;
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
public class CustomerTaskDTO {
    private Long idTask;
    private FieldOfActivity fieldOfActivity;
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime startDate;
    private LocalDateTime finishedDate;
    private LocalDateTime endDate;
    private Double cost;
    private String description;
    private Task.Status status;
    private Integer taskRate;
    private List<RespondedContractorDTO> contractors;
    private List<TaskFile> links;

    public CustomerTaskDTO(Task task, FieldOfActivity fieldOfActivity, List<RespondedContractorDTO> contractors) {
        this.idTask = task.getIdTask();
        this.fieldOfActivity = fieldOfActivity;
        this.name = task.getName();
        this.creationDate = task.getCreationDate();
        this.startDate = task.getStartDate();
        this.finishedDate = task.getFinishedDate();
        this.endDate = task.getEndDate();
        this.cost = task.getCost();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.taskRate = task.getTaskRate();
        this.contractors = contractors;
        this.links = task.getLinks();
    }
}

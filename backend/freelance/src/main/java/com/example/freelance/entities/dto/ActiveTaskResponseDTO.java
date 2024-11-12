package com.example.freelance.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveTaskResponseDTO {
    private List<ActiveTaskDTO> tasks;
    private int totalCount;
}

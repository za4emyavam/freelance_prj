package com.example.freelance.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskFile {
    private Long idFile;
    private Long idTask;
    private String link;
}

package com.example.freelance.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldOfActivity {
    private Long idField;
    private String field;
    private Boolean isActive;
}

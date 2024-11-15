package com.example.freelance.entities.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeriodRequestDTO {
    @NotNull
    @Past
    private LocalDate from;

    @NotNull
    private LocalDate to;
}

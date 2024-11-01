package com.example.freelance.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractorProfileDTO {
    private Long userId;
    private String email;
    private String name;
    private String secondName;
    private String surname;
    private String about;
    private Double rating;
    private Integer taskCompleted;
    private List<FieldRating> fieldRatingList;

    @Data
    @AllArgsConstructor
    public static class FieldRating {
        private Long fieldId;
        private String name;
        private Integer taskCompleted;
    }
}

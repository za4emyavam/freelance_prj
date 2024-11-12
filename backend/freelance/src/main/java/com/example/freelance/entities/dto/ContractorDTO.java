package com.example.freelance.entities.dto;

import com.example.freelance.entities.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractorDTO {
    private Long userId;
    private String email;
    private String name;
    private String secondName;
    private String surname;
    private String aboutMe;
    private Double rating;
    private Integer taskCompleted;
    private List<ContractorProfileDTO.FieldRating> fieldRatingList;
    private LocalDate birthday;
    private String gender;
    private String phoneNum;

    @Data
    @AllArgsConstructor
    public static class FieldRating {
        private Long fieldId;
        private String name;
        private Integer taskCompleted;
    }

    public ContractorDTO(ContractorProfileDTO profileDTO, String phoneNum, String gender, LocalDate birthday) {
        this.userId = profileDTO.getUserId();
        this.email = profileDTO.getEmail();
        this.name = profileDTO.getName();
        this.secondName = profileDTO.getSecondName();
        this.surname = profileDTO.getSurname();
        this.aboutMe = profileDTO.getAbout();
        this.rating = profileDTO.getRating();
        this.taskCompleted = profileDTO.getTaskCompleted();
        this.fieldRatingList = profileDTO.getFieldRatingList();
        this.birthday = birthday;
        this.gender = gender;
        this.phoneNum = phoneNum;
    }
}

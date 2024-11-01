package com.example.freelance.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private String email;
    private String name;
    private String secondName;
    private String surname;
    private LocalDate birthday;
    private String gender;
    private String phoneNum;
    private String aboutMe;
}

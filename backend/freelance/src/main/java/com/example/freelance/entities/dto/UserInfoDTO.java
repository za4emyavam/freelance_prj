package com.example.freelance.entities.dto;

import com.example.freelance.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private Long idUser;
    private String email;
    private String name;
    private String secondName;
    private String surname;
    private LocalDate birthday;
    private String gender;
    private String phoneNum;
    private String aboutMe;
    private User.UserRole role;
    private LocalDateTime registrationDate;

    public UserInfoDTO(User user) {
        this.idUser = user.getIdUser();
        this.email = user.getEmail();
        this.name = user.getName();
        this.secondName = user.getSecondName();
        this.surname = user.getSurname();
        this.birthday = user.getBirthday();
        this.gender = user.getGender();
        this.phoneNum = user.getPhoneNum();
        this.aboutMe = user.getAboutMe();
        this.role = user.getRole();
        this.registrationDate = user.getRegistrationDate();
    }
}

package com.example.freelance.entities.dto;

import com.example.freelance.entities.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {
    @NotEmpty(message = "Value can't be empty")
    @Email(message = "Should be email")
    private String email;

    @NotEmpty(message = "Value can't be empty")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

    @NotEmpty(message = "Value can't be empty")
    private String name;

    @NotEmpty(message = "Value can't be empty")
    private String secondName;

    @NotEmpty(message = "Value can't be empty")
    private String surname;

    @Past
    private LocalDate birthday;

    @NotEmpty(message = "Value can't be empty")
    private String gender;

    @NotEmpty(message = "Value can't be empty")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$" +
            "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$" +
            "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$", message = "Must be follow phone pattern")
    private String phoneNum;

    @NotEmpty(message = "Value can't be empty")
    private String aboutMe;

    @NotEmpty(message = "Value can't be empty")
    private String role;
}

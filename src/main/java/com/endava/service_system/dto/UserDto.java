package com.endava.service_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotEmpty
    @Pattern(regexp = "^([A-Za-z\\d]){8,}$", message = "Login must contain at least 8 chars, that includes letters and numbers.")
    private String login;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[._?!])[A-Za-z\\d._?!]{8,}$", message = "Login must contain at least 8 chars, that includes letters and numbers.")
    private String password;

    private String repeatedPassword;

    private String name;

    private String surname;
}

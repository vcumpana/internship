package com.endava.service_system.model.dto;

import com.endava.service_system.constraints.FieldsValueMatch;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data

@FieldsValueMatch.List({
        @FieldsValueMatch(field = "newPassword",
                fieldMatch = "repeatedNewPassword",
                message = "Passwords do not match!")})
public class PasswordDto {
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[._?!])[A-Za-z\\d._?!]{8,}$", message = "Password must contain at least 8 chars, that includes upper letters, numbers and special sign ")
    private String newPassword;

    private String repeatedNewPassword;

    private String token;
}

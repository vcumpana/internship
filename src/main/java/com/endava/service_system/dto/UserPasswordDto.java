package com.endava.service_system.dto;

import com.endava.service_system.constraints.FieldsValueMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldsValueMatch.List({
        @FieldsValueMatch(field = "newPassword",
                fieldMatch = "repeatedNewPassword",
                message = "Passwords do not match!")})
public class UserPasswordDto {

    private String oldPassword;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[._?!])[A-Za-z\\d._?!]{8,}$", message = "Password must contain at least 8 chars, that includes upper letters, numbers and special sign ")
    private String newPassword;

    private String repeatedNewPassword;
}

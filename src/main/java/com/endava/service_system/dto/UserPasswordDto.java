package com.endava.service_system.dto;

import com.endava.service_system.constraints.FieldsValueMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldsValueMatch.List({
        @FieldsValueMatch(field = "newPassword",
                fieldMatch = "repeatedNewPassword",
                message = "Passwords do not match!")})
public class UserPasswordDto {
    private String oldPassword;

    private String newPassword;

    private String repeatedNewPassword;
}

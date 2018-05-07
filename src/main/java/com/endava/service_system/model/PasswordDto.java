package com.endava.service_system.model;

import com.endava.service_system.constraints.FieldsValueMatch;
import lombok.Data;

@Data

@FieldsValueMatch.List({
        @FieldsValueMatch(field = "newPassword",
                fieldMatch = "repeatedNewPassword",
                message = "Passwords do not match!")})
public class PasswordDto {
    private String newPassword;

    private String repeatedNewPassword;
}

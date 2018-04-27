package com.endava.service_system.dto;

import com.endava.service_system.constraints.FieldsValueMatch;
import com.endava.service_system.constraints.NullableIfAnotherFieldHasValue;
import com.endava.service_system.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldsValueMatch.List({
        @FieldsValueMatch(field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords do not match!")})
@NullableIfAnotherFieldHasValue(firstField = "password",secondField = "status",message = "You need to provide at least password or status")
public class CredentialDTO {
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[._?!])[A-Za-z\\d._?!]{8,}$", message = "Password must contain at least 8 chars, that includes upper letters, numbers and special sign ")
    private String password;
    private String confirmPassword;
    @Nullable
    private UserStatus status;
}

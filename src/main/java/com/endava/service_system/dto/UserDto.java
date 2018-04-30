package com.endava.service_system.dto;

import com.endava.service_system.constraints.EmailInUseConstraint;
import com.endava.service_system.constraints.FieldsValueMatch;
import com.endava.service_system.constraints.UserEmailInUseConstraint;
import com.endava.service_system.constraints.UsernameInUseConstraint;
import com.endava.service_system.constraints.FieldsValueMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldsValueMatch.List({
        @FieldsValueMatch(field = "password",
                fieldMatch = "repeatedPassword",
                message = "Passwords do not match!")})
public class UserDto {

    @NotEmpty
    @Pattern(regexp = "^([A-Za-z\\d]){8,}$", message = "Login must contain at least 8 chars, that includes letters and numbers.")
    @Pattern(regexp = "^[^<'\">]+$",message = "You can't use tags and \"")
    @UsernameInUseConstraint
    private String login;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[._?!])[A-Za-z\\d._?!]{8,}$", message = "Password must contain at least 8 chars, that includes upper letters, numbers and special sign ")
    private String password;

    private String repeatedPassword;
    @Pattern(regexp = "^[^<'\">]+$",message = "You can't use tags and \"")
    private String name;
    @Pattern(regexp = "^[^<'\">]+$",message = "You can't use tags and \"")
    private String surname;

    @Email
    @UserEmailInUseConstraint
    @Pattern(regexp = "^[^<'\">]+$",message = "You can't use tags and \"")
    private String email;
}

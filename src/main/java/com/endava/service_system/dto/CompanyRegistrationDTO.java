package com.endava.service_system.dto;


import com.endava.service_system.constraints.EmailInUseConstraint;
import com.endava.service_system.constraints.FieldsValueMatch;
import com.endava.service_system.constraints.UsernameInUseConstraint;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@FieldsValueMatch.List({
        @FieldsValueMatch(field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords do not match!")})
public class CompanyRegistrationDTO {

    @NotEmpty
    private String name;

    @NotEmpty
    @UsernameInUseConstraint()
    //@Pattern(regexp = "^([A-Za-z\\d]){8,}$", message = "Username must contain at least 8 chars, that includes letters and numbers.")
    private String username;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[._?!])[A-Za-z\\d._?!]{8,}$", message = "Password must contain at least 8 chars, that includes letters and numbers.")
    private String password;

    private String confirmPassword;

    private String address;

    @NotEmpty
    @Email
    @EmailInUseConstraint()
    private String email;
}

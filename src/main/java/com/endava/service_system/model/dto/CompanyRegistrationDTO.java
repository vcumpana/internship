package com.endava.service_system.model.dto;


import com.endava.service_system.constraints.EmailInUseConstraint;
import com.endava.service_system.constraints.FieldsValueMatch;
import com.endava.service_system.constraints.NameInUseConstraint;
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
    @NameInUseConstraint
    @Pattern(regexp = "^[^<'\">]+$",message = "You can't use  tags and \"")
    private String name;

    @NotEmpty
    @UsernameInUseConstraint()
    @Pattern(regexp = "^[^<'\">]+$",message = "You can't use  tags and \"")
    private String username;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[._?!])[A-Za-z\\d._?!]{8,}$", message = "Password must contain at least 8 chars, that includes at least one number, upper case character and symbol.")
    private String password;

    private String confirmPassword;
    @Pattern(regexp = "^[^<'\">]+$",message = "You can't use tags and \"")
    private String address;

    @NotEmpty
    @Email
    @EmailInUseConstraint()
    @Pattern(regexp = "^[^<'\">]+$",message = "You can't use tags and \"")
    private String email;
}

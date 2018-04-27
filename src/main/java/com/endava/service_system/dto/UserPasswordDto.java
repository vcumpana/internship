package com.endava.service_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordDto {
    private String oldPassword;

    private String newPassword;

    private String repeatedNewPassword;
}

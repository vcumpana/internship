package com.endava.service_system.dto;

import com.endava.service_system.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminDTO {
    private String surname;
    private String email;
    private String name;
    private UserStatus status;
    private String username;
}

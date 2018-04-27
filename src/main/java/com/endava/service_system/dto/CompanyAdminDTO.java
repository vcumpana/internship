package com.endava.service_system.dto;

import com.endava.service_system.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyAdminDTO {
    private String companyName;
    private String username;
    private String email;
    private String bankAccount;
    private String address;
    private UserStatus status;
}

package com.endava.service_system.model.dto;

import com.endava.service_system.model.enums.UserStatus;
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
    private long bankAccount;
    private String address;
    private UserStatus status;
}

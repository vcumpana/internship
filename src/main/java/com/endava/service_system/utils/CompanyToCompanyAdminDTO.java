package com.endava.service_system.utils;

import com.endava.service_system.dto.CompanyAdminDTO;
import com.endava.service_system.model.Company;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CompanyToCompanyAdminDTO implements Converter<Company,CompanyAdminDTO> {
    @Override
    public CompanyAdminDTO convert(Company company) {
        CompanyAdminDTO dto=new CompanyAdminDTO();
        dto.setUsername(company.getCredential().getUsername());
        dto.setCompanyName(company.getName());
        dto.setStatus(company.getCredential().getStatus());
        dto.setAddress(company.getAddress());
        dto.setBankAccount(company.getBankAccount());
        dto.setEmail(company.getCredential().getEmail());
        return dto;
    }
}

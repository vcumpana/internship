package com.endava.service_system.converter;

import com.endava.service_system.model.dto.CompanyAdminDTO;
import com.endava.service_system.model.entities.Company;
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
        dto.setEmail(company.getCredential().getEmail());
        if(company.getCredential()!=null&&company.getCredential().getBankAccount()!=null)
        dto.setBankAccount(company.getCredential().getBankAccount().getCountNumber());
        return dto;
    }
}

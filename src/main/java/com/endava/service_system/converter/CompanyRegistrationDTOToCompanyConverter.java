package com.endava.service_system.converter;

import com.endava.service_system.model.dto.CompanyRegistrationDTO;
import com.endava.service_system.model.entities.Company;
import com.endava.service_system.model.entities.Credential;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.endava.service_system.model.enums.Role.ROLE_COMPANY;
import static com.endava.service_system.model.enums.UserStatus.WAITING;

@Component
public class CompanyRegistrationDTOToCompanyConverter implements Converter<CompanyRegistrationDTO, Company> {

    @Override
    public Company convert(CompanyRegistrationDTO companyRegistrationDTO) {
        Company company = new Company();
        Credential credential = new Credential();
        company.setName(companyRegistrationDTO.getName());
        credential.setUsername(companyRegistrationDTO.getUsername());
        credential.setPassword(companyRegistrationDTO.getPassword());
        company.setAddress(companyRegistrationDTO.getAddress());
        credential.setEmail(companyRegistrationDTO.getEmail());
        credential.setStatus(WAITING);
        credential.setRole(ROLE_COMPANY);
        company.setCredential(credential);
        return company;
    }
}

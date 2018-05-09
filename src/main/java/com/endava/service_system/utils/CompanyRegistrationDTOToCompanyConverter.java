package com.endava.service_system.utils;

import com.endava.service_system.dto.CompanyRegistrationDTO;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.Credential;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.endava.service_system.enums.UserStatus.ACCEPTED;
import static com.endava.service_system.enums.UserStatus.WAITING;
import static com.endava.service_system.model.Role.ROLE_COMPANY;

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

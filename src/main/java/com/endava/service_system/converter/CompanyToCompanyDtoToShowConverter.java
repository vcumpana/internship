package com.endava.service_system.converter;

import com.endava.service_system.model.dto.CompanyDtoToShow;
import com.endava.service_system.model.dto.UserDtoToShow;
import com.endava.service_system.model.entities.Company;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CompanyToCompanyDtoToShowConverter implements Converter<Company, CompanyDtoToShow> {
    @Override
    public CompanyDtoToShow convert(Company company) {
        CompanyDtoToShow companyDtoToShow = new CompanyDtoToShow();
        companyDtoToShow.setName(company.getName());
        companyDtoToShow.setAddress(company.getAddress());
        companyDtoToShow.setEmail(company.getCredential().getEmail());
        return companyDtoToShow;
    }
}

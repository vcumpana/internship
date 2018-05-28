package com.endava.service_system.converter;

import com.endava.service_system.model.dto.CompanyForUserDto;
import com.endava.service_system.model.entities.Company;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CompanyToCompanyForUserDtoConverter implements Converter<Company,CompanyForUserDto> {
    @Override
    public CompanyForUserDto convert(Company source) {
        return new CompanyForUserDto(source.getId(),source.getName());
    }
}

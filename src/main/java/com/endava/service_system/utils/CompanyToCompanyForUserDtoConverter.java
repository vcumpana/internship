package com.endava.service_system.utils;

import com.endava.service_system.model.Company;
import com.endava.service_system.dto.CompanyForUserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CompanyToCompanyForUserDtoConverter implements Converter<Company,CompanyForUserDto> {
    @Override
    public CompanyForUserDto convert(Company source) {
        return new CompanyForUserDto(source.getId(),source.getName());
    }
}

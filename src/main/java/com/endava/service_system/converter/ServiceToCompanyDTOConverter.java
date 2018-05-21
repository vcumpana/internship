package com.endava.service_system.converter;

import com.endava.service_system.model.dto.ServiceToCompanyDto;
import com.endava.service_system.model.dto.ServiceToUserDto;
import com.endava.service_system.model.entities.Service;
import com.endava.service_system.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ServiceToCompanyDTOConverter implements Converter<Service, ServiceToCompanyDto> {

    @Override
    public ServiceToCompanyDto convert(Service service){
        ServiceToCompanyDto serviceDto = new ServiceToCompanyDto();
        serviceDto.setCategory(service.getCategory());
        serviceDto.setId(service.getId());
        serviceDto.setDescription(service.getDescription());
        serviceDto.setPrice(service.getPrice());
        serviceDto.setTitle(service.getTitle());

        return serviceDto;
    }
}

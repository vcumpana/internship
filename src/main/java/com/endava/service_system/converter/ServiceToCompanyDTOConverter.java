package com.endava.service_system.converter;

import com.endava.service_system.model.dto.ServiceToCompanyDto;
import com.endava.service_system.model.entities.Service;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

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

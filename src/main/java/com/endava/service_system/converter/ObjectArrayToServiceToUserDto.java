package com.endava.service_system.converter;

import com.endava.service_system.model.dto.ServiceToUserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ObjectArrayToServiceToUserDto implements Converter<Object[],ServiceToUserDto> {
    //expects in order c.name,s.id,s.title,cat.name,s.description,s.price,company url ,image name
    @Override
    public ServiceToUserDto convert(Object[] source) {
        ServiceToUserDto serviceToUserDto=new ServiceToUserDto();
        serviceToUserDto.setCompanyName((String) source[0]);
        serviceToUserDto.setId((Long) source[1]);
        serviceToUserDto.setTitle((String) source[2]);
        serviceToUserDto.setCategory((String) source[3]);
        serviceToUserDto.setDescription((String) source[4]);
        serviceToUserDto.setPrice((BigDecimal) source[5]);
        serviceToUserDto.setCompanyUrl((String) source[6]);
        serviceToUserDto.setImageName(((String) source[7]));
        return serviceToUserDto;
    }
}

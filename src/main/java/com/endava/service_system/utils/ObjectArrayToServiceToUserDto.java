package com.endava.service_system.utils;

import com.endava.service_system.dto.ServiceToUserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ObjectArrayToServiceToUserDto implements Converter<Object[],ServiceToUserDto> {
    //expects in order c.name,s.id,s.title,cat.name,s.description,s.price,company url ,image name
    @Override
    public ServiceToUserDto convert(Object[] source) {
        ServiceToUserDto serviceToUserDto=new ServiceToUserDto();
//        Pattern pattern = Pattern.compile(".(.*?).");
//        Matcher matcher = pattern.matcher((String)source[6]);
        serviceToUserDto.setCompanyName((String) source[0]);
        serviceToUserDto.setId((Long) source[1]);
        serviceToUserDto.setTitle((String) source[2]);
        serviceToUserDto.setCategory((String) source[3]);
        serviceToUserDto.setDescription((String) source[4]);
        serviceToUserDto.setPrice((BigDecimal) source[5]);
        serviceToUserDto.setCompanyUrl((String) source[6]);
//        if (matcher.find())
//            serviceToUserDto.setImageName(matcher.group(1));
        serviceToUserDto.setImageName(((String) source[7]));
        return serviceToUserDto;
    }
}

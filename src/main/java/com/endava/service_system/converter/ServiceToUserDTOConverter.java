package com.endava.service_system.converter;

import com.endava.service_system.model.dto.ServiceToUserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ServiceToUserDTOConverter implements Converter<Map, ServiceToUserDto> {
    @Override
    public ServiceToUserDto convert(Map map){
            ServiceToUserDto service = new ServiceToUserDto();
        Pattern pattern = Pattern.compile("\\.(.*?)\\.");
        Matcher matcher = pattern.matcher((String)map.get("url"));
            service.setCategory((String)map.get("category"));
            service.setId(((BigInteger)map.get("id")).longValue());
            service.setCompanyName((String)map.get("companyName"));
            service.setDescription((String)map.get("description"));
            service.setPrice((BigDecimal) map.get("price"));
            service.setTitle((String)map.get("title"));
            if (matcher.find())
                service.setImageName(matcher.group(1));
            service.setCompanyUrl((String)map.get("url"));
        return service;
    }
}

package com.endava.service_system.converter;

import com.endava.service_system.model.dto.ServiceToUserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

@Component
public class ServiceToUserDTOConverter implements Converter<Map, ServiceToUserDto> {
    @Override
    public ServiceToUserDto convert(Map map){
            ServiceToUserDto service = new ServiceToUserDto();
            service.setCategory((String)map.get("category"));
            service.setId(((BigInteger)map.get("id")).longValue());
            service.setCompanyName((String)map.get("companyName"));
            service.setDescription((String)map.get("description"));
            service.setPrice((BigDecimal) map.get("price"));
            service.setTitle((String)map.get("title"));
            if(map.get("image_name")!=null)
            service.setImageName((String)map.get("image_name"));
            if(map.get("company_url")!=null)
            service.setCompanyUrl((String)map.get("company_url"));
        return service;
    }
}

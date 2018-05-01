package com.endava.service_system.utils;

import com.endava.service_system.dto.ServiceToUserDto;
import com.endava.service_system.dto.UserDto;
import com.endava.service_system.model.Credential;
import com.endava.service_system.model.Role;
import com.endava.service_system.model.Service;
import com.endava.service_system.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.endava.service_system.enums.UserStatus.WAITING;

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
        return service;
    }
}

package com.endava.service_system.utils;

import com.endava.service_system.dto.ContractToUserDto;
import com.endava.service_system.dto.ServiceToUserDto;
import com.endava.service_system.enums.ContractStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ObjectArrayToContractToUserDto implements Converter<Object[],ContractToUserDto> {
    //expects in order cont.id,comp.name,s.title,cat.name,s.price,cont.startDate,cont.endDate,cont.status
    @Override
    public ContractToUserDto convert(Object[] source) {
        ContractToUserDto contractToUserDto=new ContractToUserDto();
        contractToUserDto.setId((Integer) source[0]);
        contractToUserDto.setCompanyName((String) source[1]);
        contractToUserDto.setServiceTitle((String) source[2]);
        contractToUserDto.setCategoryName((String) source[3]);
        contractToUserDto.setServicePrice((BigDecimal) source[4]);
        contractToUserDto.setStartDate((LocalDate) source[5]);
        contractToUserDto.setEndDate((LocalDate) source[6]);
        contractToUserDto.setContractStatus((ContractStatus) source[7]);
        return contractToUserDto;
    }
}

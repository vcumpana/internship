package com.endava.service_system.converter;

import com.endava.service_system.model.dto.ContractForShowingDto;
import com.endava.service_system.model.enums.ContractStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ObjectArrayToContractToUserDto implements Converter<Object[],ContractForShowingDto> {
    //expects in order cont.id,comp.name,s.title,cat.name,s.price,cont.startDate,cont.endDate,cont.status
    @Override
    public ContractForShowingDto convert(Object[] source) {
        ContractForShowingDto contractForShowingDto =new ContractForShowingDto();
        contractForShowingDto.setId((Long) source[0]);
        contractForShowingDto.setCompanyName((String) source[1]);
        contractForShowingDto.setServiceTitle((String) source[2]);
        contractForShowingDto.setCategoryName((String) source[3]);
        contractForShowingDto.setServicePrice((BigDecimal) source[4]);
        contractForShowingDto.setStartDate((LocalDate) source[5]);
        contractForShowingDto.setEndDate((LocalDate) source[6]);
        contractForShowingDto.setContractStatus((ContractStatus) source[7]);
        contractForShowingDto.setFullName((String) source[8]);
        return contractForShowingDto;
    }
}

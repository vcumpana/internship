package com.endava.service_system.converter;

import com.endava.service_system.model.dto.ContractDtoFromUser;
import com.endava.service_system.model.entities.Contract;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.endava.service_system.model.enums.ContractStatus.SIGNEDBYCLIENT;

@Component
@NoArgsConstructor
public class ContractDtoFromUserConverter implements Converter<ContractDtoFromUser, Contract> {

    @Override
    public Contract convert(ContractDtoFromUser contractDto){
        Contract contract = new Contract();
        contract.setStartDate(LocalDate.parse(contractDto.getStartDate()));
        contract.setEndDate(LocalDate.parse(contractDto.getEndDate()));
        contract.setStatus(SIGNEDBYCLIENT);
        return contract;
    }
}

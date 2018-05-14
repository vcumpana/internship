package com.endava.service_system.utils;

import com.endava.service_system.dto.ContractDtoFromUser;
import com.endava.service_system.enums.ContractStatus;
import com.endava.service_system.model.Contract;
import com.endava.service_system.model.Role;
import com.endava.service_system.model.User;
import com.endava.service_system.service.CompanyService;
import com.endava.service_system.service.ServiceService;
import com.endava.service_system.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.endava.service_system.enums.ContractStatus.SIGNEDBYCLIENT;
import static com.endava.service_system.enums.UserStatus.WAITING;

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

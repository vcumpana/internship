package com.endava.service_system.service;

import com.endava.service_system.dao.CompanyDao;
import com.endava.service_system.dao.ContractDao;
import com.endava.service_system.dao.ContractsToUserDao;
import com.endava.service_system.dto.ContractDtoFromUser;
import com.endava.service_system.dto.ContractToUserDto;
import com.endava.service_system.model.Contract;
import com.endava.service_system.model.ContractForUserDtoFilter;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {
    private CompanyDao companyDao;
    private ConversionService conversionService;
    private ContractDao contractDao;
    private ServiceService serviceService;
    private UserService userService;
    private ContractsToUserDao contractsToUserDao;

    public ContractService(CompanyDao companyDao, ConversionService conversionService, ContractDao contractDao, ServiceService serviceService, UserService userService, ContractsToUserDao contractsToUserDao) {
        this.companyDao = companyDao;
        this.conversionService = conversionService;
        this.contractDao = contractDao;
        this.serviceService = serviceService;
        this.userService = userService;
        this.contractsToUserDao = contractsToUserDao;
    }

    public Contract saveContract(ContractDtoFromUser contractDto) {
        Contract contract;
        contract = conversionService.convert(contractDto, Contract.class);
        contract.setService(serviceService.getServiceById(contractDto.getServiceId()).get());
        contract.setCompany(companyDao.getByName(contractDto.getCompanyName()).get());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        contract.setUser(userService.getByUsername(username).get());
        return contractDao.save(contract);
    }

    public List<ContractToUserDto> getUserContracts(ContractForUserDtoFilter filter) {
        return contractsToUserDao.getUserContracts(filter);
    }
}

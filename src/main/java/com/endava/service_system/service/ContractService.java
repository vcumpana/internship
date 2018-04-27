package com.endava.service_system.service;

import com.endava.service_system.dao.CompanyDao;
import com.endava.service_system.dao.ContractDao;
import com.endava.service_system.dto.ContractDtoFromUser;
import com.endava.service_system.model.Contract;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ContractService {
    private CompanyDao companyDao;
    private ConversionService conversionService;
    private ContractDao contractDao;
    private ServiceService serviceService;
    private UserService userService;

    public ContractService(CompanyDao companyDao, ConversionService conversionService, ContractDao contractDao, ServiceService serviceService, UserService userService) {
        this.companyDao = companyDao;
        this.conversionService = conversionService;
        this.contractDao = contractDao;
        this.serviceService = serviceService;
        this.userService = userService;
    }

    public Contract saveContract(ContractDtoFromUser contractDto) {
        Contract contract = new Contract();
        contract = conversionService.convert(contractDto, Contract.class);
        contract.setService(serviceService.getServiceById(contractDto.getServiceId()).get());
        contract.setCompany(companyDao.getByName(contractDto.getCompanyName()).get());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        contract.setUser(userService.getByUsername(username).get());
        return contractDao.save(contract);
    }
}

package com.endava.service_system.service;

import com.endava.service_system.dao.CompanyDao;
import com.endava.service_system.dao.ContractDao;
import com.endava.service_system.dao.ContractsToUserDao;
import com.endava.service_system.dto.ContractDtoFromUser;
import com.endava.service_system.dto.ContractToUserDto;
import com.endava.service_system.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import com.endava.service_system.model.ContractForUserDtoFilter;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import java.util.List;

@Service
public class ContractService {
    private CompanyDao companyDao;
    private ConversionService conversionService;
    private ContractDao contractDao;
    private ServiceService serviceService;
    private UserService userService;
    private ContractsToUserDao contractsToUserDao;

    public Contract saveContract(ContractDtoFromUser contractDto) {
        Contract contract;
        contract = conversionService.convert(contractDto, Contract.class);
        contract.setService(serviceService.getServiceById(contractDto.getServiceId()).get());
        contract.setCompany(companyDao.getByName(contractDto.getCompanyName()).get());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        contract.setUser(userService.getByUsername(username).get());
        return contractDao.save(contract);
    }

    public Contract getContractById(long id){
        return contractDao.getContractWithDetails(id);
    }

    public List<Contract> getAllContractsByCompanyUsername(String companyUsername){
        return contractDao.getAllContractsByCompanyUsername(companyUsername);
    }

    @Autowired
    public void setCompanyDao(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Autowired
    public void setContractDao(ContractDao contractDao) {
        this.contractDao = contractDao;
    }

    @Autowired
    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public List<ContractToUserDto> getUserContracts(ContractForUserDtoFilter filter) {
        return contractsToUserDao.getUserContracts(filter);
    }
}

package com.endava.service_system.service;

import com.endava.service_system.dao.CompanyDao;
import com.endava.service_system.dao.ContractDao;
import com.endava.service_system.dao.ContractsToUserDao;
import com.endava.service_system.dto.ContractDtoFromUser;
import com.endava.service_system.dto.ContractForShowingDto;
import com.endava.service_system.model.Contract;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.endava.service_system.model.ContractForUserDtoFilter;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {
    private static final Logger LOGGER= LogManager.getLogger(ContractService.class);
    private CompanyDao companyDao;
    private ConversionService conversionService;
    private ContractDao contractDao;
    private ServiceService serviceService;
    private UserService userService;
    private ContractsToUserDao contractsToUserDao;

    public Contract saveContract(ContractDtoFromUser contractDto) {
        System.out.println(contractDto);
        Contract contract;
        contract = conversionService.convert(contractDto, Contract.class);
        contract.setService(serviceService.getServiceById(contractDto.getServiceId()).get());
        contract.setCompany(companyDao.getByName(contractDto.getCompanyName()).get());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        LOGGER.log(Level.DEBUG,"username:"+username);
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
    public void setContractsToUserDao(ContractsToUserDao contractsToUserDao) {
        this.contractsToUserDao = contractsToUserDao;
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

    public List<ContractForShowingDto> getContracts(ContractForUserDtoFilter filter) {
        return contractsToUserDao.getContracts(filter);
    }

    public Long getPagesSizeForFilter(ContractForUserDtoFilter filter){
        return contractsToUserDao.getPagesSizeForFilter(filter);
    }

    public void update(Contract contract) {
        contractDao.save(contract);
    }
}

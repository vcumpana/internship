package com.endava.service_system.service;

import com.endava.service_system.dao.CompanyDao;
import com.endava.service_system.dao.ContractDao;
import com.endava.service_system.dao.ContractUpdateDao;
import com.endava.service_system.dao.ContractsToUserDao;
import com.endava.service_system.model.dto.ContractDtoFromUser;
import com.endava.service_system.model.dto.ContractForShowingDto;
import com.endava.service_system.model.entities.Contract;
import com.endava.service_system.model.enums.ContractStatus;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.endava.service_system.model.filters.ContractForUserDtoFilter;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.endava.service_system.model.enums.ContractStatus.EXPIRED;

@Service
public class ContractService {
    private static final Logger LOGGER= LogManager.getLogger(ContractService.class);
    private CompanyDao companyDao;
    private ConversionService conversionService;
    private ContractDao contractDao;
    private ServiceService serviceService;
    private InvoiceService invoiceService;
    private UserService userService;
    private ContractsToUserDao contractsToUserDao;
    private ContractUpdateDao contractUpdateDao;

    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
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

    @Autowired
    public void setContractUpdateDao(ContractUpdateDao contractUpdateDao) {
        this.contractUpdateDao = contractUpdateDao;
    }

    public Contract saveContract(ContractDtoFromUser contractDto) {
        LOGGER.debug(contractDto);
        Contract contract;
        contract = conversionService.convert(contractDto, Contract.class);
        contract.setService(serviceService.getServiceById(contractDto.getServiceId()).get());
        contract.setCompany(companyDao.getByName(contractDto.getCompanyName()).get());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        LOGGER.log(Level.DEBUG,"username:"+username);
        contract.setUser(userService.getByUsername(username).get());
        if(contractDao.checkIfSuchContractExists(contract.getCompany(), contract.getUser(), contract.getService(), contract.getStartDate()) == 0) {
            return contractDao.save(contract);
        } else {
            return null;
        }
    }

    public Contract getContractById(long id){
        return contractDao.getContractWithDetails(id);
    }

    public List<Contract> getAllContractsByCompanyUsername(String companyUsername){
        return contractDao.getAllContractsByCompanyUsername(companyUsername);
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

    public Long[] getAllContractsIdsByCompanyUsername(String username) {

        long[] invoicesIds = contractDao.getAllIds(username);
        List<Long> longList=new ArrayList<>();
        for(Long id:invoicesIds){
            longList.add(id);
        }
        List<Long> ids= invoiceService.canCreateInvoices(longList);

        return ids.toArray(new Long[ids.size()]);
    }

    public List<Contract> getActiveContractsThatHaveEndDateBefore(LocalDate today, int amountOfNotificationsAtOnce) {
        return contractUpdateDao.getActiveContractsThatHaveEndDateBefore(today, amountOfNotificationsAtOnce);
    }

    public void makeContractsExpired(List<Long> ids) {
        contractDao.setStatus(EXPIRED,ids);
    }
}

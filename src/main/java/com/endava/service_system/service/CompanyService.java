package com.endava.service_system.service;

import com.endava.service_system.dao.CompanyDao;
import com.endava.service_system.dao.InvoiceEntityManagerDao;
import com.endava.service_system.dto.ContractDtoFromUser;
import com.endava.service_system.dto.CredentialDTO;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.Invoice;
import com.endava.service_system.utils.AuthUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import com.endava.service_system.enums.UserStatus;
import com.endava.service_system.model.Credential;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.endava.service_system.model.Service;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CompanyService {
    private static final Logger LOGGER= LogManager.getLogger(CompanyService.class);
    private CompanyDao companyDao;
    private CredentialService credentialService;
    private ConversionService conversionService;
    private AuthUtils authUtils;
    private ServiceService serviceService;

    public Company save(Company company) {
        credentialService.encodePassword(company.getCredential());
        Credential credential = credentialService.save(company.getCredential());
        company.setCredential(credential);
        return companyDao.save(company);
    }

    public Company updateWithoutCredentials(Company company){
        return companyDao.save(company);
    }

    public List<Company> getAll() {
        return companyDao.findAll();
    }

    public Optional<Company> getCompanyByUsername(String username) {
        return companyDao.getByUsername(username);
    }

    public List<Company> getAllWithStatus(UserStatus status) {
        return companyDao.getAllWithStatus(status);
    }

    public Optional<Company> getCompanyById(int id) {
        return companyDao.getById(id);
    }

    public Optional<Company> getCompanyByName(String name) {
        return companyDao.getByName(name);
    }

    public List<Company> getAllCompanies() {
        return companyDao.getAll();
    }

    Optional<Company> getCompanyByNameWithServices(String name){
        LOGGER.log(Level.DEBUG,"companyName:"+name);
        return companyDao.getCompanyByNameWithServices(name);
    }

    public Optional<Company> getCompanyByEmail(String email) {
        return companyDao.getByEmail(email);
    }

    public Optional<Company> getCompanyNameByUsername(String name) {
        return companyDao.getByUsername(name);
    }

    public void addNewService(Service service) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        LOGGER.log(Level.DEBUG,username);
        Company company = companyDao.getByUsername(username).get();
        LOGGER.log(Level.DEBUG,company);
        List<Service> services = serviceService.getServicesByCompanyName(company.getName());
        services.add(service);
        company.setServices(services);
        companyDao.save(company);
    }

    @Autowired
    public void setCompanyDao(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Autowired
    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Autowired
    public void setAuthUtils(AuthUtils authUtils) {
        this.authUtils = authUtils;
    }

}

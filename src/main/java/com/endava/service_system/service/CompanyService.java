package com.endava.service_system.service;

import com.endava.service_system.dao.CompanyDao;
import com.endava.service_system.model.entities.Company;
import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.model.entities.Service;
import com.endava.service_system.model.enums.UserStatus;
import com.endava.service_system.utils.AuthUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Map<String,Object> getAll(Pageable page, UserStatus userStatus){
        Map<String,Object> map=new HashMap<>();
        long total;
        if(userStatus!=null) {
            total = companyDao.countByCredentialStatus(userStatus);
        }else{
            total=companyDao.count();
        }
        long pages=total/page.getPageSize();
        if(total%page.getPageSize()!=0){
            pages++;
        }
        map.put("pages",pages);
        if(userStatus!=null) {
            map.put("companies", companyDao.getByCredentialStatus(page, userStatus));
        }else{
            map.put("companies", companyDao.getAllBy(page));
        }
        return map;
    }

    public void updateService(Service service) {
        String username = authUtils.getAuthenticatedUsername();
        LOGGER.log(Level.DEBUG,username);
        Company company = companyDao.getByUsername(username).get();
        LOGGER.log(Level.DEBUG,company);
        LOGGER.debug("trying to update : "+service);
        serviceService.saveService(service);
    }

    public void deleteServiceByIdFromCompany(long id) {
        String username = authUtils.getAuthenticatedUsername();
        LOGGER.log(Level.DEBUG,username);
        Company company = companyDao.getByUsername(username).get();
        LOGGER.log(Level.DEBUG,company);
        List<Service> services = serviceService.getServicesByCompanyName(company.getName());
        for (int i = 0; i< services.size(); i++){
            if (id == services.get(i).getId())
                services.remove(i);
        }
        company.setServices(services);
        companyDao.save(company);
    }

    public String getImageNameByCompanyUsername(String username) {
        return companyDao.getImageName(username);
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

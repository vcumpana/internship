package com.endava.service_system.service;

import com.endava.service_system.dao.CompanyDao;
import com.endava.service_system.dto.ContractDtoFromUser;
import com.endava.service_system.dto.CredentialDTO;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import com.endava.service_system.dao.CompanyDao;
import com.endava.service_system.enums.UserStatus;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.Credential;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private CompanyDao companyDao;
    private CredentialService credentialService;

    public Company save(Company company) {
        Credential credential = credentialService.save(company.getCredential());
        company.setCredential(credential);
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

    Optional<Company> getCompanyByNameWithServices(String name){
        return companyDao.getCompanyByNameWithServices(name);
    }

    public Optional<Company> getCompanyByEmail(String email) {
        return companyDao.getByEmail(email);
    }

    public int updateStatusAndPassword(String name, CredentialDTO credential) {
        Optional<String> username=companyDao.getCredentialUsernameByName(name);
        if(username.isPresent()){
            return credentialService.updateStatusAndPassword(username.get(),credential);
        }else{
            username.orElseThrow(()->new RuntimeException("Company not found"));
        }
        return 0;
    }

    @Autowired
    public void setCompanyDao(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }
}

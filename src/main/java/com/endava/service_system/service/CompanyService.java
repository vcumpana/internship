package com.endava.service_system.service;

import com.endava.service_system.dao.CompanyDao;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private CompanyDao companyDao;
    private CredentialService credentialService;

    public CompanyService(CompanyDao companyDao, CredentialService credentialService) {
        this.companyDao = companyDao;
        this.credentialService = credentialService;
    }

    public void saveCompany(Company company) {
        credentialService.save(company.getCredential());
        companyDao.save(company);
    }

    public Optional<Company> getCompanyByEmail(String email) {
        return companyDao.getByEmail(email);
    }

    public Optional<Company> getCompanyByUsername(String username) {
        return companyDao.getByUsername(username);
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
        return companyDao.getCompanyByNameWithServices(name);
    }
}

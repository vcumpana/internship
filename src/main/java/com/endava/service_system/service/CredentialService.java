package com.endava.service_system.service;

import com.endava.service_system.dao.ICredentialDao;
import com.endava.service_system.model.Credential;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CredentialService {
    private final ICredentialDao credentialDao;

    public CredentialService(ICredentialDao credentialDao) {
        this.credentialDao = credentialDao;
    }

    public Optional<Credential> getByUsername(String username){
        return credentialDao.getByUsername(username);
    }

    public void save(Credential credential){
        credentialDao.save(credential);
    }
}

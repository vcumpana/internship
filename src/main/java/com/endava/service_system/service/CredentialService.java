package com.endava.service_system.service;

import com.endava.service_system.dao.ICredentialDao;
import com.endava.service_system.dto.CredentialDTO;
import com.endava.service_system.enums.UserStatus;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.Credential;
import com.endava.service_system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredentialService {
    private ICredentialDao credentialDao;
    private PasswordEncoder passwordEncoder;

    public Optional<Credential> getByUsername(String username) {
        return credentialDao.getByUsername(username);
    }

    public Credential save(Credential credential) {
        encodePassword(credential);
        return credentialDao.save(credential);
    }

    private void encodePassword(Credential credential){
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
    }

    public int updateStatusAndPassword(String username,CredentialDTO credential) {
        String password=credential.getPassword();
        UserStatus status=credential.getStatus();
        boolean passwordIsPresent=password!=null&&!password.isEmpty();
        boolean statusIsPresent=status!=null;
        if(passwordIsPresent){
            password=passwordEncoder.encode(password);
        }
        if(passwordIsPresent&&statusIsPresent){
            return updatePasswordAndStatus(username,password,status);
        }else if(passwordIsPresent){
            return updatePassword(username,password);
        }else if(statusIsPresent){
            return updateStatus(username,status);
        }
        return 0;

    }

    private int updateStatus(String username,UserStatus status){
        return credentialDao.updateStatus(username,status);
    }

    public int updatePassword(String username,String password){
        return credentialDao.updatePassword(username,password);
    }

    private int updatePasswordAndStatus(String username,String password,UserStatus status){
        return credentialDao.updateStatusAndPassword(username,password,status);
    }

    @Autowired
    public void setCredentialDao(ICredentialDao credentialDao){
        this.credentialDao=credentialDao;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder=passwordEncoder;
    }


    public Optional<Credential> getByEmail(String email) {
        return credentialDao.getByEmail(email);
    }

}

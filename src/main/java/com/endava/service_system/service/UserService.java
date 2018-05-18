package com.endava.service_system.service;

import com.endava.service_system.dao.UserDao;
import com.endava.service_system.model.dto.UserDtoToShow;
import com.endava.service_system.model.enums.UserStatus;
import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private UserDao userDao;
    private CredentialService credentialService;
    private BankService bankService;
    public void saveUser(User user){
        System.out.println("user save in saveUser UserService :"+user);
        //credentialService.save(user.getCredential());
        bankService.addBankAccount(user.getCredential());
        credentialService.encodePassword(user.getCredential());
        User saveUser=userDao.save(user);
        System.out.println("user saved in saveUser UserService :"+saveUser);
    }

    public User updateUserWithoutCredentials(User user){
        return userDao.save(user);
    }

    public Optional<User> getByUsername(String username){
        return userDao.getByUsername(username);
    }

    public Optional<User> getByEmail(String email){
        return userDao.getByEmail(email);
    }

    public void updateUserNameAndSurname(String username, UserDtoToShow userDtoToShow){
        User user = userDao.getByUsername(username).get();
        user.setName(userDtoToShow.getName());
        user.setSurname(userDtoToShow.getSurname());
        userDao.save(user);
    }

    public void updateUserPassword(String username, String newPassword){
        Credential credential = credentialService.getByUsername(username).get();
        credential.setPassword(newPassword);
        credentialService.encodePassword(credential);
        credentialService.save(credential);
    }

    public List<User> getAllWithCredentials(){
        return userDao.getAllWithCredentials();
    }

    public List<User> getAllWithCredentialsAndStatus(UserStatus status) {
        return userDao.getAllWithCredentialsAndStatus(status);
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Autowired
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }
}

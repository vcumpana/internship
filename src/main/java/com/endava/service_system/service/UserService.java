package com.endava.service_system.service;

import com.endava.service_system.dao.UserDao;
import com.endava.service_system.model.dto.UserDtoToShow;
import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.model.entities.User;
import com.endava.service_system.model.enums.UserStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private static final Logger LOGGER=LogManager.getLogger(UserService.class);
    private UserDao userDao;
    private CredentialService credentialService;
    private BankService bankService;

    public void saveUser(User user) throws InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        LOGGER.debug("user save in saveUser UserService :"+user);
        //credentialService.save(user.getCredential());
        bankService.addBankAccount(user.getCredential());
        credentialService.encodePassword(user.getCredential());
        User saveUser=userDao.save(user);
        LOGGER.debug("user saved in saveUser UserService :"+saveUser);
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

    public Map<String,Object> getAll(Pageable page, UserStatus userStatus){
        Map<String,Object> map=new HashMap<>();
        long total;
        if(userStatus!=null) {
            total = userDao.countByCredentialStatus(userStatus);
        }else{
            total=userDao.count();
        }
        long pages=total/page.getPageSize();
        if(total%page.getPageSize()!=0){
            pages++;
        }
        map.put("pages",pages);
        if(userStatus!=null) {
            map.put("users", userDao.getByCredentialStatus(page, userStatus));
        }else{
            map.put("users", userDao.getAllBy(page));
        }
        return map;
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

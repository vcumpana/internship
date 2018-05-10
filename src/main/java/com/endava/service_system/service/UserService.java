package com.endava.service_system.service;

import com.endava.service_system.dao.UserDao;
import com.endava.service_system.dto.CredentialDTO;
import com.endava.service_system.dto.UserDto;
import com.endava.service_system.dto.UserDtoToShow;
import com.endava.service_system.enums.UserStatus;
import com.endava.service_system.model.BankAccount;
import com.endava.service_system.model.Credential;
import com.endava.service_system.model.Role;
import com.endava.service_system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    public void saveUser(User user) {
        Credential credential = credentialService.save(user.getCredential());
        bankService.addBankAccount(credential);
        userDao.save(user);
    }

    public Optional<User> getByUsername(String username) {
        return userDao.getByUsername(username);
    }

    public Optional<User> getByEmail(String email) {
        return userDao.getByEmail(email);
    }

    public void updateUserNameAndSurname(String username, UserDtoToShow userDtoToShow) {
        User user = userDao.getByUsername(username).get();
        user.setName(userDtoToShow.getName());
        user.setSurname(userDtoToShow.getSurname());
        userDao.save(user);
    }

    public void updateUserPassword(String username, String newPassword) {
        Credential credential = credentialService.getByUsername(username).get();
        credential.setPassword(newPassword);
        credentialService.save(credential);
    }

    public List<User> getAllWithCredentials() {
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

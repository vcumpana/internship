package com.endava.service_system.service;

import com.endava.service_system.dao.UserDao;
import com.endava.service_system.dto.UserDto;
import com.endava.service_system.dto.UserDtoToShow;
import com.endava.service_system.model.Credential;
import com.endava.service_system.model.Role;
import com.endava.service_system.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private UserDao userDao;
    private CredentialService credentialService;

    public UserService(UserDao userDao, CredentialService credentialService) {
        this.userDao = userDao;
        this.credentialService = credentialService;
    }

    public void saveUser(User user){
        credentialService.save(user.getCredential());
        userDao.save(user);
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
        credential.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        credentialService.save(credential);
    }
}

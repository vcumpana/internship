package com.endava.service_system.service;

import com.endava.service_system.dao.UserDao;
import com.endava.service_system.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void saveUser(User user){
        userDao.save(user);
    }

    public Optional<User> getByUsername(String username){
        return userDao.getByUsername(username);
    }
}

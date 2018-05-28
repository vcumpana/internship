package com.endava.service_system.service;

import com.endava.service_system.dao.ICredentialDao;
import com.endava.service_system.dao.UserDao;
import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.model.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

public class UserServiceTest {

    private UserService userService;
    private UserDao userDao;
    private BankService bankService;
    private CredentialService credentialService;
    private PasswordEncoder passwordEncoder;
    private ICredentialDao credentialDao;

    @Before
    public void init() {
        userService = new UserService();
        userDao = Mockito.mock(UserDao.class);
        userService.setUserDao(userDao);
        bankService = Mockito.mock(BankService.class);
        userService.setBankService(bankService);
        credentialService = new CredentialService();
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        credentialService.setPasswordEncoder(passwordEncoder);
        credentialDao = Mockito.mock(ICredentialDao.class);
        credentialService.setCredentialDao(credentialDao);
        userService.setCredentialService(credentialService);
    }

    @Test
    public void saveUser() throws IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        User user = new User();
        Credential credential = new Credential();
        String password = "ss";
        String encodedPassword = "$2a$10$EjEKWplBez4v.ZBBryOVdeovHyHj0DlpYdptGqbGu51XKBhbRe6Ua";
        user.setCredential(credential);
        credential.setPassword(password);
        Mockito.when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        userService.saveUser(user);

        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(password);
        Mockito.verify(userDao, Mockito.times(1)).save(user);
    }

}
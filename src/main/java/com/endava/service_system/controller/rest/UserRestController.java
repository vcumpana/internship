package com.endava.service_system.controller.rest;

import com.endava.service_system.dto.*;
import com.endava.service_system.enums.UserStatus;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.User;
import com.endava.service_system.service.CredentialService;
import com.endava.service_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserRestController {

    private UserService userService;
    private CredentialService credentialService;
    private PasswordEncoder passwordEncoder;
    private ConversionService conversionService;

    @PutMapping(value = "/user/selfUpdate")
    public void updateUser(@RequestBody UserDtoToShow userDtoToShow) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.updateUserNameAndSurname(username, userDtoToShow);
    }

    @PutMapping(value = "/user/selfUpdatePassword")
    public ResponseEntity updateUserPassword(@RequestBody UserPasswordDto userPasswordDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUsername(username).get();
        if (passwordEncoder.matches(userPasswordDto.getOldPassword(), user.getCredential().getPassword())) {
            userService.updateUserPassword(username, userPasswordDto.getNewPassword());
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity getAllUsers(@RequestParam(required = false, value = "status") UserStatus status) {
        List<User> users = getAllUsersWithStatus(status);
        if (!users.isEmpty()) {
            List<UserAdminDTO> usersForAdmin = users.stream()
                    .map((user) -> conversionService.convert(user, UserAdminDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity(usersForAdmin, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    private List<User> getAllUsersWithStatus(UserStatus status) {
        if (status == null) {
            return userService.getAllWithCredentials();
        }else{
            return userService.getAllWithCredentialsAndStatus(status);
        }
    }

    @PutMapping("/admin/users/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity changePasswordWithStatus(@PathVariable("username") String username,
                                                   @Validated @RequestBody CredentialDTO credentialDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            int entitiesUpdated = credentialService.updateStatusAndPassword(username, credentialDTO);
            String json = "{\"count\":\"1\"}";
            return new ResponseEntity(json, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }
}

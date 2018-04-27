package com.endava.service_system.controller;

import com.endava.service_system.dto.UserDtoToShow;
import com.endava.service_system.dto.UserPasswordDto;
import com.endava.service_system.model.User;
import com.endava.service_system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRestController {

    private UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

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
        if (new BCryptPasswordEncoder().matches(userPasswordDto.getOldPassword(), user.getCredential().getPassword())) {
            userService.updateUserPassword(username, userPasswordDto.getNewPassword());
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}

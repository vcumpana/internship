package com.endava.service_system.controller;

import com.endava.service_system.dto.UserDto;
import com.endava.service_system.model.User;
import com.endava.service_system.service.UserService;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private UserService userService;
    private ConversionService conversionService;

    public UserController(UserService userService, ConversionService conversionService) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @RequestMapping(value = "/register")
    public String goToRegistrationPage(){
        return "userRegistration";
    }

    @PostMapping(value = "/register")
    public String register(@ModelAttribute("user") UserDto userDto){
        userService.saveUser(conversionService.convert(userDto, User.class));
        return "userRegistration";
    }
}

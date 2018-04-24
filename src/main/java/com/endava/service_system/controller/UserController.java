package com.endava.service_system.controller;

import com.endava.service_system.dto.UserDto;
import com.endava.service_system.model.User;
import com.endava.service_system.service.UserService;
import com.endava.service_system.utils.AuthUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class UserController {

    private UserService userService;
    private ConversionService conversionService;
    private AuthUtils authUtils;

    public UserController(UserService userService, ConversionService conversionService, AuthUtils authUtils) {
        this.userService = userService;
        this.conversionService = conversionService;
        this.authUtils = authUtils;
    }

    @GetMapping("/user")
    public String adminIndex() {
        return "redirect:/user/login";
    }

    @GetMapping("/user/login")
    public String userLogin(Authentication auth) {
        if(authUtils.isLoggedIn(auth)) {
            return authUtils.isUser(auth)?"redirect:/user/panel":"redirect:/";
        }
        return "userLogin";
    }

    @RequestMapping(value = "/user/register")
    public String goToRegistrationPage(){
        return "userRegistration";
    }

    @PostMapping(value = "/user/register")
    public String register(@ModelAttribute("user") UserDto userDto){
        userService.saveUser(conversionService.convert(userDto, User.class));
        return "redirect:/user/login";
    }

    @GetMapping(value = "/user/panel")
    public String userPanel(){
        return  "userPanel";
    }
}

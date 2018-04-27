package com.endava.service_system.controller;

import com.endava.service_system.dto.UserDto;
import com.endava.service_system.dto.UserDtoToShow;
import com.endava.service_system.model.User;
import com.endava.service_system.service.UserService;
import com.endava.service_system.utils.AuthUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
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
            return authUtils.isUser(auth)?"redirect:/user/profile":"redirect:/";
        }
        return "login";
    }

    @RequestMapping(value = "/user/registration")
    public String goToRegistrationPage(Model model){
        model.addAttribute("user", new UserDto());
        return "userRegistration";
    }

    @PostMapping(value = "/user/registration")
    public String register(Model model, @ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            model.addAttribute("user", userDto);
            return "userRegistration";
        }
        userService.saveUser(conversionService.convert(userDto, User.class));
        return "redirect:/user/login";
    }

    @GetMapping(value = "/user/profile")
    public String userProfile(Model model){
        addUsernameToModel(model);
        return  "userProfile";
    }

    @GetMapping(value = "/user/serviceList")
    public String userServiceList(Model model){
        addUsernameToModel(model);
        return  "userServiceList";
    }

    @GetMapping(value = "/user/cabinet")
    public String userCabinet(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDtoToShow user = conversionService.convert(userService.getByUsername(username).get(), UserDtoToShow.class);
        model.addAttribute("username", username);
        model.addAttribute("user", user);
        return "userCabinet";
    }

    private void addUsernameToModel(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }
}

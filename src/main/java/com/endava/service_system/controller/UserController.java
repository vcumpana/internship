package com.endava.service_system.controller;

import com.endava.service_system.model.dto.UserDto;
import com.endava.service_system.model.dto.UserDtoToShow;
import com.endava.service_system.model.entities.User;
import com.endava.service_system.service.CategoryService;
import com.endava.service_system.service.CompanyService;
import com.endava.service_system.service.UserService;
import com.endava.service_system.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

@Controller
public class UserController {

    private UserService userService;
    private ConversionService conversionService;
    private AuthUtils authUtils;
    private CategoryService categoryService;
    private CompanyService companyService;

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
    public String register(Model model, @ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult) throws InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
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
        addCategoriesToModel(model);
        addCompaniesToModel(model);
        return  "userProfile";
    }

    @GetMapping(value = "/user/serviceList")
    public String userServiceList(Model model){
        addUsernameToModel(model);
        addCategoriesToModel(model);
        addCompaniesToModel(model);
        return  "userServiceList";
    }

    @GetMapping(value = "/user/invoicesPage")
    public String userInvoices(Model model){
        addUsernameToModel(model);
        addCategoriesToModel(model);
        addCompaniesToModel(model);
        return "userInvoices";
    }

    @GetMapping(value = "/user/notifications")
    public String userNotifications(Model model){
        addUsernameToModel(model);
        return "userNotifications";
    }

    @GetMapping(value = "/user/statements")
    public String userStatements(Model model){
        addUsernameToModel(model);
        return "userStatement";
    }

    private void addCompaniesToModel(Model model) {
        model.addAttribute("companies",companyService.getAll());
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

    private void addCategoriesToModel(Model model){
        model.addAttribute("categories",categoryService.getAll());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Autowired
    public void setAuthUtils(AuthUtils authUtils) {
        this.authUtils = authUtils;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }
}

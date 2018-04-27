package com.endava.service_system.controller;

import com.endava.service_system.utils.AuthUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Controller
public class CommonController {
    private final AuthUtils authUtils;

    public CommonController(AuthUtils authUtils) {
        this.authUtils = authUtils;
    }

    @GetMapping("/login")
    public String loginView(Authentication auth) {
        return redirectIfLoggedIn(auth).orElse("adminLogin");
    }

    @GetMapping("/success")
    public String redirectAfterLogin(Authentication auth) {
        return redirectIfLoggedIn(auth).orElse("redirect:/index");
    }

    private Optional<String> redirectIfLoggedIn(Authentication auth){
        if(authUtils.isLoggedIn(auth)) {
            if(authUtils.isAdmin(auth)){
                return Optional.of("redirect:/admin/panel");
            }else if(authUtils.isUser(auth)){
                return Optional.of("redirect:/user/profile");
            }else if(authUtils.isCompany(auth)){
                return Optional.of("redirect:/company/profile");
            }
        }
        return Optional.empty();
    }

}

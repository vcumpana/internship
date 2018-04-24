package com.endava.service_system.controller;

import com.endava.service_system.utils.AuthUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class CommonController {
    private final AuthUtils authUtils;

    public CommonController(AuthUtils authUtils) {
        this.authUtils = authUtils;
    }

    @GetMapping("/login")
    public String adminLogin(Authentication auth) {
        if(authUtils.isLoggedIn(auth)) {
            if(authUtils.isAdmin(auth)){
                return "redirect:/admin/panel";
            }else if(authUtils.isUser(auth)){
                return "redirect:/user/profile";
            }else if(authUtils.isCompany(auth)){
                return "redirect:/company/profile";
            }
        }
        return "adminLogin";
    }

    @GetMapping("/success")
    public String redirectAfterLogin(Authentication auth) {
        if(authUtils.isLoggedIn(auth)) {
            if(authUtils.isAdmin(auth)){
                return "redirect:/admin/panel";
            }else if(authUtils.isUser(auth)){
                return "redirect:/user/profile";
            }else if(authUtils.isCompany(auth)){
                return "redirect:/company/profile";
            }
        }
        return "redirect:/index";
    }
}

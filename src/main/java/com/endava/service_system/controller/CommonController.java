package com.endava.service_system.controller;

import com.endava.service_system.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CommonController {
    private final AuthUtils authUtils;

    @GetMapping("/login")
    public String loginView(Authentication auth) {
        return redirectIfLoggedIn(auth).orElse("adminLogin");
    }

    @GetMapping("/success")
    public String redirectAfterLogin(Authentication auth) {
        return redirectIfLoggedIn(auth).orElse("redirect:/index");
    }

    private Optional<String> redirectIfLoggedIn( Authentication auth){
        if(authUtils.isLoggedIn(auth)) {
            if(authUtils.isAdmin(auth)){
                return Optional.of("redirect:/admin/panel");
            }else if(authUtils.isUser(auth)){
                return Optional.of("redirect:/user/profile");
            }else if(authUtils.isCompany(auth)){
                return Optional.of("redirect:/company/cabinet");
            }
        }
        return Optional.empty();
    }
}

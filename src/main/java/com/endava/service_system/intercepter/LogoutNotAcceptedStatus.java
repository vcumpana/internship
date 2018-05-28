package com.endava.service_system.intercepter;

import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.model.enums.UserStatus;
import com.endava.service_system.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class LogoutNotAcceptedStatus extends HandlerInterceptorAdapter {

    private CredentialService credentialService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Credential> credentials = credentialService.getByUsername(username);
        credentials.orElseThrow(()->new RuntimeException("You included a path that doesn't have credentials"));
        if (credentials.get().getStatus() != UserStatus.ACCEPTED) {
            response.sendRedirect("/logout");
        }
        return true;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }
}
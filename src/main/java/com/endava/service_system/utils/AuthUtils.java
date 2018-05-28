package com.endava.service_system.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.endava.service_system.model.enums.Role.*;
@Component
public class AuthUtils {
	
	public boolean isLoggedIn(Authentication auth) {
		return auth!=null&&auth.isAuthenticated();
	}
	
	public boolean isAdmin(Authentication auth) {
		return !auth.getAuthorities().isEmpty()&&hasRole(auth,ROLE_ADMIN.name());
	}

	public boolean isUser(Authentication auth) {
		return !auth.getAuthorities().isEmpty()&&hasRole(auth,ROLE_USER.name());
	}

	public boolean isCompany(Authentication auth){
		return !auth.getAuthorities().isEmpty()&&hasRole(auth,ROLE_COMPANY.name());
	}

	public boolean hasRole(Authentication auth,String role) {
		return auth.getAuthorities()
				.stream()
				.anyMatch(g->g.getAuthority().equals(role));
	}

	public String getAuthenticatedUsername(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}

}

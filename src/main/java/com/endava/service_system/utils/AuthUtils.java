package com.endava.service_system.utils;

import static com.endava.service_system.model.Role.ROLE_ADMIN;
import static com.endava.service_system.model.Role.ROLE_COMPANY;
import static com.endava.service_system.model.Role.ROLE_USER;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
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

}

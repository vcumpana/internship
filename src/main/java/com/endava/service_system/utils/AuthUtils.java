package com.endava.service_system.utils;

import static com.endava.service_system.model.Roles.ROLE_ADMIN;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
@Component
public class AuthUtils {
	
	public boolean isLoggedIn(Authentication auth) {
		return auth!=null&&auth.isAuthenticated();
	}
	
	public boolean isAdmin(Authentication auth) {
		return !auth.getAuthorities().isEmpty()&&hasRole(auth,ROLE_ADMIN);
	}
	
	public boolean hasRole(Authentication auth,String role) {
		return auth.getAuthorities()
				.stream()
				.anyMatch(g->g.getAuthority().equals(role));
	}

}

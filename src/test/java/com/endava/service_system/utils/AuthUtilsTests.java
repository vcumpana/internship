package com.endava.service_system.utils;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class AuthUtilsTests {

    private AuthUtils authUtils=new AuthUtils();
    @Test
    public void userNotLoggedInAuthNull(){
        assertFalse(authUtils.isLoggedIn(null));
    }

    @Test
    public void userNotLoggedInAuthExistsButIsNotAuthenticated(){
        Authentication authentication= Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);
        assertFalse(authUtils.isLoggedIn(authentication));
    }

    @Test
    public void userAuthenticated(){
        Authentication authentication= Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        assertTrue(authUtils.isLoggedIn(authentication));
    }

    @Test
    public void userIsAdmin(){
        Authentication authentication= Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        Collection<GrantedAuthority> authorities=Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Mockito.doReturn(authorities).when(authentication).getAuthorities();
        assertTrue(authUtils.isAdmin(authentication));
        assertFalse(authUtils.isUser(authentication));
        assertFalse(authUtils.isCompany(authentication));
    }

    @Test
    public void authenticationWhenUserWasLogged(){
        Authentication authentication= Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        Collection<GrantedAuthority> authorities=Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        Mockito.doReturn(authorities).when(authentication).getAuthorities();
        assertFalse(authUtils.isAdmin(authentication));
        assertTrue(authUtils.isUser(authentication));
        assertFalse(authUtils.isCompany(authentication));
    }

    @Test
    public void authenticationWhenCompanyWasLogged(){
        Authentication authentication= Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        Collection<GrantedAuthority> authorities=Arrays.asList(new SimpleGrantedAuthority("ROLE_COMPANY"));
        Mockito.doReturn(authorities).when(authentication).getAuthorities();
        assertFalse(authUtils.isAdmin(authentication));
        assertFalse(authUtils.isUser(authentication));
        assertTrue(authUtils.isCompany(authentication));
    }

    @Test
    public void testGetUserName(){
        Authentication authentication=Mockito.mock(Authentication.class);
        String login="loginswt";
        when(authentication.getName()).thenReturn(login);
        SecurityContext securityContext=Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        assertEquals(authUtils.getAuthenticatedUsername(),login);
    }


}

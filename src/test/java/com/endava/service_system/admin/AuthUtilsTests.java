package com.endava.service_system.admin;

import com.endava.service_system.utils.AuthUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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


}

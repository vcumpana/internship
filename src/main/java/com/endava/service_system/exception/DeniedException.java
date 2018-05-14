package com.endava.service_system.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DeniedException extends RuntimeException {
    private static final String MESSAGE="You have been denied to login,please contact administrators ";
    public DeniedException(String message){
        super(message);
    }

    public DeniedException(){
        super(MESSAGE);
    }
}

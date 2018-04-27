package com.endava.service_system.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DeniedException extends RuntimeException {
    public DeniedException(String message){
        super(message);
    }
}

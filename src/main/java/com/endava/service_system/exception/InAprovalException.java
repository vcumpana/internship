package com.endava.service_system.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class InAprovalException extends RuntimeException {

    public InAprovalException(String message){
        super(message);
    }
}

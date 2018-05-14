package com.endava.service_system.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class InAprovalException extends RuntimeException {
    private static final String MESSAGE=" Please wait for admin approval";

    public InAprovalException(String message){
        super(message);
    }

    public InAprovalException(){
        super(MESSAGE);
    }
}

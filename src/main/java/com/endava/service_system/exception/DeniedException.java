package com.endava.service_system.exception;

import org.springframework.security.core.AuthenticationException;

public class DeniedException extends AuthenticationException {
    private static final String MESSAGE="You have been denied to login,please contact administrators ";

    public DeniedException(){
        super(MESSAGE);
    }

}

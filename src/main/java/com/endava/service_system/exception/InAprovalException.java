package com.endava.service_system.exception;

import org.springframework.security.core.AuthenticationException;

public class InAprovalException extends AuthenticationException {
    private static final String MESSAGE=" Please wait for admin approval";

    public InAprovalException(){
        super(MESSAGE);
    }

}

package com.endava.service_system.exception;

import org.springframework.security.core.AuthenticationException;

public class WrongUsernameException extends AuthenticationException {
    private static final String MESSAGE="You have mistyped your username";

    public WrongUsernameException(){
        super(MESSAGE);
    }

}

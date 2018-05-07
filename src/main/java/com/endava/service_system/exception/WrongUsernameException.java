package com.endava.service_system.exception;

public class WrongUsernameException extends RuntimeException {
    private static final String MESSAGE="You have mistyped your username";
    public WrongUsernameException(){
        super(MESSAGE);
    }

}

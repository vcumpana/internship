package com.endava.service_system.exception;

public class BadRequestException extends Exception {

    public BadRequestException(String jsonMessage){
        super(jsonMessage);
    }

}

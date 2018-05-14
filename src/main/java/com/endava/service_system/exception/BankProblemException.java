package com.endava.service_system.exception;

public class BankProblemException  extends RuntimeException{
    private final static String MESSAGE="Bank Problem";
    public BankProblemException(){
        super(MESSAGE);
    }
}

package com.sebrs3018.SmartSharing.Exceptions;

public class InvalidPasswordException extends Exception{

    public InvalidPasswordException(){}

    public InvalidPasswordException(String errorMessage){
        super(errorMessage);
    }

    public InvalidPasswordException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}

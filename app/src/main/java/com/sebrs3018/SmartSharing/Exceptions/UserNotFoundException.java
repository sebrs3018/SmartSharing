package com.sebrs3018.SmartSharing.Exceptions;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(){}
    public UserNotFoundException(String errorMessage){
        super(errorMessage);
    }
    public UserNotFoundException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}

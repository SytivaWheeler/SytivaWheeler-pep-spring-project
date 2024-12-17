package com.example.exception;

public class DuplicateUserException extends RuntimeException {
    
    public DuplicateUserException(){
        super();
    }

    public DuplicateUserException(String errorMessage){
        super(errorMessage);
    }
}

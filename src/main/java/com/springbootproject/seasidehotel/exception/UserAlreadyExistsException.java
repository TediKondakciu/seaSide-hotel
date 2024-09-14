package com.springbootproject.seasidehotel.exception;

/**
 * @author Tedi Kondakçiu
 */

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message){
        super(message);
    }
}

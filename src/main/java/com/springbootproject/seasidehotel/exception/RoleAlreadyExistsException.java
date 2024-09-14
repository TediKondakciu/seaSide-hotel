package com.springbootproject.seasidehotel.exception;

/**
 * @author Tedi Kondakçiu
 */

public class RoleAlreadyExistsException extends RuntimeException {
    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}

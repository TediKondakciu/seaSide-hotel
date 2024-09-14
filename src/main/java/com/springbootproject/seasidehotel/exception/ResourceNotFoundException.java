package com.springbootproject.seasidehotel.exception;

/**
 * @author Tedi Kondakçiu
 */

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

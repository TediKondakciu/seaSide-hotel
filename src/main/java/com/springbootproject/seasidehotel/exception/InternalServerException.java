package com.springbootproject.seasidehotel.exception;

/**
 * @author Tedi Kondakçiu
 */

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}

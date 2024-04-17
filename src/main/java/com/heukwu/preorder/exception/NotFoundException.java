package com.heukwu.preorder.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(ErrorMessage message) {
        super(message.getMessage());
    }
}

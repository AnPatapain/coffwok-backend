package com.anpatapain.coffwok.chat.exception;

public class UnAuthorizedActionException extends RuntimeException{
    public UnAuthorizedActionException(String message) {
        super(message);
    }
}

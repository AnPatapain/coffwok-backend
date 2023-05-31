package com.anpatapain.coffwok.security.oauth2;

import org.springframework.security.core.AuthenticationException;

public class OAuth2ProcessingException extends AuthenticationException {

    public OAuth2ProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public OAuth2ProcessingException(String msg) {
        super(msg);
    }
}

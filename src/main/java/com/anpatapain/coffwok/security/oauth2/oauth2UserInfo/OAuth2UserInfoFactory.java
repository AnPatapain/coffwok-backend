package com.anpatapain.coffwok.security.oauth2.oauth2UserInfo;

import com.anpatapain.coffwok.user.model.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        throw  new RuntimeException("Unknown authorization server");
    }
}

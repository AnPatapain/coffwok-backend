package com.anpatapain.coffwok.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix="app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oAuth2 = new OAuth2();

    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationSec;

        public long getTokenExpirationSec() {
            return tokenExpirationSec;
        }

        public void setTokenExpirationSec(long tokenExpirationSec) {
            this.tokenExpirationSec = tokenExpirationSec;
        }

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }
    }

    public static class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }
    }

    public Auth getAuth() {
        return auth;
    }

    public OAuth2 getOAuth2() {
        return oAuth2;
    }
}

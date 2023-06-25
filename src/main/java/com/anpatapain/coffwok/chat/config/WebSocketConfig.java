package com.anpatapain.coffwok.chat.config;

import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.security.jwt.CustomUserDetailsService;
import com.anpatapain.coffwok.security.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private String client_app_url = "http://localhost:5173";
//    private String client_app_url = "https://www.coffwok.com";

    private Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    private JwtUtils jwtUtils;

    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public WebSocketConfig(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
        logger.info("Web Socket config created with args-constructor");
    }

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/chatroom");
        registry.setApplicationDestinationPrefixes("/api");

    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/web-socket-endpoint")
                .setAllowedOrigins(client_app_url)
                .withSockJS();
    }

}

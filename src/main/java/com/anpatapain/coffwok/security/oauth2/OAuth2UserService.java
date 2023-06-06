package com.anpatapain.coffwok.security.oauth2;

import com.anpatapain.coffwok.security.UserPrincipal;
import com.anpatapain.coffwok.security.oauth2.oauth2UserInfo.OAuth2UserInfo;
import com.anpatapain.coffwok.security.oauth2.oauth2UserInfo.OAuth2UserInfoFactory;
import com.anpatapain.coffwok.user.repository.UserRepository;
import com.anpatapain.coffwok.user.model.AuthProvider;
import com.anpatapain.coffwok.user.model.Role;
import com.anpatapain.coffwok.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(OAuth2UserService.class);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        }catch (AuthenticationException authenticationException) {
            throw authenticationException;
        }catch (Exception exception) {
            throw new InternalAuthenticationServiceException(exception.getMessage(), exception.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes()
        );

        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2ProcessingException("Email not found from provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2ProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        }else {
            user = registerUser(oAuth2UserRequest, oAuth2UserInfo);
        }
        return UserPrincipal.build(user, oAuth2UserInfo.getAttributes());
    }
    private User registerUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setRole(Role.ROLE_USER);
        logger.info("register triggered: " + oAuth2UserInfo.getImageUrl());

        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        //UPDATE LOGIC HERE
        logger.info("update triggered: " + oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

}

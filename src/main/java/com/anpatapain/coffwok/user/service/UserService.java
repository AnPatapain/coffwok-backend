package com.anpatapain.coffwok.user.service;

import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.security.UserPrincipal;
import com.anpatapain.coffwok.user.model.User;
import com.anpatapain.coffwok.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            return userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("user", "id", userPrincipal.getId()));
        }
        return null;
    }
}

package com.anpatapain.coffwok.user.controller;

import com.anpatapain.coffwok.security.UserPrincipal;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if( !(authentication instanceof AnonymousAuthenticationToken) ) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return ResponseEntity.ok(
                    userRepository.findById(userPrincipal.getId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("user", "id", userPrincipal.getId())
                    )
            );
        }
        return ResponseEntity.status(401).body("Role must be user and The token must be valid");
    }
}

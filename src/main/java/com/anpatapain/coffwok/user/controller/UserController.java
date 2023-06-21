package com.anpatapain.coffwok.user.controller;

import com.anpatapain.coffwok.security.UserPrincipal;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.user.model.User;
import com.anpatapain.coffwok.user.repository.UserRepository;
import com.anpatapain.coffwok.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserRepository userRepository;
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> get() {
        try {
            User user = userService.getCurrentAuthenticatedUser();
            if(user != null) {
                return ResponseEntity.ok(user);
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error when get current user");
            }
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getOneById(@PathVariable String id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        try{
            User user = userService.getUserById(id);
            userService.deleteUser(id);
            return ResponseEntity.ok("User delete successfully");
        }catch (ResourceNotFoundException e){
            return  ResponseEntity.badRequest().body(e.getMessage()+"user not found");
        }
    }
}

package com.anpatapain.coffwok.profile.controller;

import com.anpatapain.coffwok.common.payload.response.ApiResponse;
import com.anpatapain.coffwok.image_upload.service.ImageStorageService;
import com.anpatapain.coffwok.profile.model.Profile;
import com.anpatapain.coffwok.profile.dto.ProfileInfoDTO;
import com.anpatapain.coffwok.profile.service.ProfileService;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.user.repository.UserRepository;
import com.anpatapain.coffwok.user.model.User;
import com.anpatapain.coffwok.user.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private ProfileService profileService;

    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(ProfileController.class);


    @Autowired
    public ProfileController(ProfileService profileService,
                             UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentProfile() {
        User user;
        try{
            user = userService.getCurrentAuthenticatedUser();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }
        if(user.getProfileId() == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("profile not found for user");
        }

        try {
            EntityModel<Profile> profileEntity = profileService.getOne(user.getProfileId());
            return ResponseEntity.ok(profileEntity);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public CollectionModel<EntityModel<Profile>> all() {
        List<EntityModel<Profile>> profileEntities = profileService.getAll();
        return CollectionModel.of(profileEntities, linkTo(methodOn(ProfileController.class).all()).withRel("profiles"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> one(@PathVariable String id) {
        try {
            EntityModel<Profile> profileEntity = profileService.getOne(id);
            return ResponseEntity.ok(profileEntity);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }

    @PostMapping(value = "")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createOne(@Valid @RequestBody ProfileInfoDTO profileInfoDto) {
        User user;
        try{
            user = userService.getCurrentAuthenticatedUser();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }

        if(user.getProfileId() == null) {
            EntityModel<Profile> profileEntityModel = profileService.createProfile(user, profileInfoDto);
            return ResponseEntity.ok(profileEntityModel);
        }else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User has already profile"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> putOne(@PathVariable String id, @Valid @RequestBody ProfileInfoDTO updatedProfileInfoDTO) {
        User user;
        try{
            user = userService.getCurrentAuthenticatedUser();
            if(user.getProfileId() == null || !user.getProfileId().equals(id)) {
                return ResponseEntity.badRequest().body("you are not the owner of this profile");
            }
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }

        EntityModel<Profile> updatedProfileEntity;
        try {
            updatedProfileEntity = profileService.putProfile(id, updatedProfileInfoDTO);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
        return ResponseEntity.ok(updatedProfileEntity);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> patchOne(@PathVariable String id, @RequestBody ProfileInfoDTO partialUpdatedProfileInfoDTO) {
        User user;
        try{
            user = userService.getCurrentAuthenticatedUser();
            if(user.getProfileId() == null || !user.getProfileId().equals(id)) {
                return ResponseEntity.badRequest().body("you are not the owner of this profile");
            }
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }

        EntityModel<Profile> updatedProfileEntity;
        try {
            updatedProfileEntity = profileService.patchProfile(id, partialUpdatedProfileInfoDTO);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
        return ResponseEntity.ok(updatedProfileEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteOne(@PathVariable String id) {
        User user;
        try{
            user = userService.getCurrentAuthenticatedUser();
            if(user.getProfileId() == null || !user.getProfileId().equals(id)) {
                return ResponseEntity.badRequest().body("you are not the owner of this profile");
            }
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }

        profileService.deleteProfile(id);
        return ResponseEntity.ok("Profile deleted successfully");
    }

}

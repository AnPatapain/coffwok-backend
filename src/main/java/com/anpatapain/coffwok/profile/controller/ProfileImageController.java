package com.anpatapain.coffwok.profile.controller;

import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.common.payload.response.ApiResponse;
import com.anpatapain.coffwok.image_upload.exception.ImageUploadException;
import com.anpatapain.coffwok.image_upload.service.ImageStorageService;
import com.anpatapain.coffwok.profile.model.Profile;
import com.anpatapain.coffwok.profile.repository.ProfileRepository;
import com.anpatapain.coffwok.profile.service.ProfileService;
import com.anpatapain.coffwok.user.model.User;
import com.anpatapain.coffwok.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profiles/images")
public class ProfileImageController {
    private Logger logger = LoggerFactory.getLogger(ProfileImageController.class);
    private UserService userService;
    private ProfileService profileService;


    @Autowired
    public ProfileImageController(UserService userService,
                                  ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    @PostMapping(value = "/{profileId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> uploadProfileImage(@PathVariable String profileId, @RequestPart("file") MultipartFile imageFile) {
        User user;
        try{
            user = userService.getCurrentAuthenticatedUser();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }

        if(!user.getProfileId().equals(profileId)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User is not owner of this profile"));
        }

        EntityModel<Profile> profileEntityModel;
        try {
            profileEntityModel = profileService.uploadImage(profileId, imageFile);
        }catch (ImageUploadException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
        return ResponseEntity.ok(profileEntityModel);
    }
}

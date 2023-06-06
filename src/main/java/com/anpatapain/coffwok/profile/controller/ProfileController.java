package com.anpatapain.coffwok.profile.controller;

import com.anpatapain.coffwok.common.payload.response.ApiResponse;
import com.anpatapain.coffwok.image_upload.exception.ImageUploadException;
import com.anpatapain.coffwok.image_upload.service.ImageStorageService;
import com.anpatapain.coffwok.profile.model.Profile;
import com.anpatapain.coffwok.profile.dto.ProfileDTO;
import com.anpatapain.coffwok.profile.service.ProfileService;
import com.anpatapain.coffwok.profile.service.ProfileServiceImpl;
import com.anpatapain.coffwok.security.UserPrincipal;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.user.repository.UserRepository;
import com.anpatapain.coffwok.user.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private UserRepository userRepository;

    private ProfileService profileService;

    private ImageStorageService storageService;

    private Logger logger = LoggerFactory.getLogger(ProfileController.class);


    @Autowired
    public ProfileController(UserRepository userRepository, ProfileService profileService, ImageStorageService storageService) {
        this.userRepository = userRepository;
        this.profileService = profileService;
        this.storageService = storageService;
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
        EntityModel<Profile> profileEntity = profileService.getOne(id);
        return ResponseEntity.ok(profileEntity);
    }

    @PostMapping(value = "", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createOne(@RequestPart("profile") String profileDTO,
                                       @RequestPart("file") MultipartFile imageFile) {
        User user;
        try{
            user = getCurrentAuthenticatedUser();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }

        if(user.getProfileId() != null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User has already profile"));
        }

        ProfileDTO profileDtoJson = new ProfileDTO();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            profileDtoJson = objectMapper.readValue(profileDTO, ProfileDTO.class);
        }catch (IOException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        EntityModel<Profile> profileEntityModel;
        try {
            profileEntityModel = profileService.createProfileWithImage(user, profileDtoJson, imageFile);
        }catch (ConstraintViolationException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (ImageUploadException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(profileEntityModel);
    }
//    public ResponseEntity<?> createOne(@Valid @RequestBody ProfileDTO profileDTO) {
//        User user;
//        try{
//            user = getCurrentAuthenticatedUser();
//        }catch (ResourceNotFoundException e) {
//            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
//        }
//
//        if(user.getProfileId() == null) {
//            EntityModel<Profile> profileEntityModel = profileService.createProfile(user, profileDTO);
//            return ResponseEntity.ok(profileEntityModel);
//        }else {
//            return ResponseEntity.badRequest().body(new ApiResponse(false, "User has already profile"));
//        }
//    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> putOne(@PathVariable String id, @Valid @RequestBody ProfileDTO updatedProfileDTO) {
        EntityModel<Profile> updatedProfileEntity = profileService.putProfile(id, updatedProfileDTO);
        return ResponseEntity.ok(updatedProfileEntity);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> patchOne(@PathVariable String id, @RequestBody ProfileDTO partialUpdatedProfileDTO) {
        EntityModel<Profile> updatedProfileEntity = profileService.patchProfile(id, partialUpdatedProfileDTO);
        return ResponseEntity.ok(updatedProfileEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteOne(@PathVariable String id) {
        profileService.deleteProfile(id);
        return ResponseEntity.ok("Profile deleted successfully");
    }

    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            return userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("user", "id", userPrincipal.getId()));
        }
        return null;
    }

}

package com.anpatapain.coffwok.profile;

import com.anpatapain.coffwok.common.payload.response.ApiResponse;
import com.anpatapain.coffwok.security.UserPrincipal;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.user.UserRepository;
import com.anpatapain.coffwok.user.model.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private ProfileRepository profileRepository;

    private UserRepository userRepository;

    private ProfileService profileService;

    private ProfileAssembler profileAssembler;

    @Autowired
    public ProfileController(ProfileRepository profileRepository, UserRepository userRepository,
                             ProfileAssembler profileAssembler, ProfileService profileService) {
        this.profileRepository = profileRepository;
        this.profileAssembler = profileAssembler;
        this.userRepository = userRepository;
        this.profileService = profileService;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public CollectionModel<EntityModel<Profile>> all() {
        List<EntityModel<Profile>> profileEntities = profileRepository.findAll()
                .stream()
                .map(profileAssembler::toModel)
                .toList();
        return CollectionModel.of(profileEntities, linkTo(methodOn(ProfileController.class).all()).withRel("profiles"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> one(@PathVariable String id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));
        return ResponseEntity.ok(profileAssembler.toModel(profile));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createOne(@Valid @RequestBody ProfileDTO profileDTO) {
        User user;
        try{
            user = getCurrentAuthenticatedUser();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }

        if(user.getProfileId() == null) {
            Profile profile = profileService.createProfile(user, profileDTO);

            EntityModel<Profile> profileEntityModel = profileAssembler.toModel(profile);
            return ResponseEntity.ok(profileEntityModel);
        }else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User has already profile"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> putOne(@PathVariable String id, @Valid @RequestBody ProfileDTO updatedProfileDTO) {
        Profile updatedProfile = profileService.putProfile(id, updatedProfileDTO);
        return ResponseEntity.ok(profileAssembler.toModel(updatedProfile));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> patchOne(@PathVariable String id, @RequestBody ProfileDTO partialUpdatedProfileDTO) {
        Profile updatedProfile = profileService.patchProfile(id, partialUpdatedProfileDTO);
        return ResponseEntity.ok(profileAssembler.toModel(updatedProfile));
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

            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("user", "id", userPrincipal.getId()));
            return user;
        }
        return null;
    }

}

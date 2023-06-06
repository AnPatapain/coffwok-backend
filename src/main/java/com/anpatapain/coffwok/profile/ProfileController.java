package com.anpatapain.coffwok.profile;

import com.anpatapain.coffwok.common.payload.response.ApiResponse;
import com.anpatapain.coffwok.security.UserPrincipal;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.user.UserRepository;
import com.anpatapain.coffwok.user.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
    private UserRepository userRepository;

    private ProfileService profileService;


    @Autowired
    public ProfileController(UserRepository userRepository, ProfileService profileService) {
        this.userRepository = userRepository;
        this.profileService = profileService;
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
            EntityModel<Profile> profileEntityModel = profileService.createProfile(user, profileDTO);
            return ResponseEntity.ok(profileEntityModel);
        }else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User has already profile"));
        }
    }

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

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

    private ProfileAssembler profileAssembler;

    @Autowired
    public ProfileController(ProfileRepository profileRepository, UserRepository userRepository, ProfileAssembler profileAssembler) {
        this.profileRepository = profileRepository;
        this.profileAssembler = profileAssembler;
        this.userRepository = userRepository;
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
            Profile profile = new Profile(
                    profileDTO.getName(),
                    profileDTO.getAbout(),
                    profileDTO.getDob_day(),
                    profileDTO.getDob_month(),
                    profileDTO.getDob_year(),
                    profileDTO.getSchool(),
                    profileDTO.getStrength_subjects(),
                    profileDTO.getWeak_subjects()
            );

            profile.setUserId(user.getId());
            profile = profileRepository.save(profile);

            user.setProfileId(profile.getId());
            user = userRepository.save(user);

            EntityModel<Profile> profileEntityModel = profileAssembler.toModel(profile);
            return ResponseEntity.ok(profileEntityModel);
        }else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User has already profile"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> putOne(@PathVariable String id, @Valid @RequestBody ProfileDTO updatedProfileDTO) {
        try{
            Profile existingProfile = profileRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));

            updateProfileProperties(existingProfile, updatedProfileDTO);

            Profile updatedProfile = profileRepository.save(existingProfile);

            return ResponseEntity.ok(profileAssembler.toModel(updatedProfile));
        } catch (Exception e) {
            // Handle any potential exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the profile.");
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> patchOne(@PathVariable String id, @RequestBody ProfileDTO partialUpdatedProfileDTO) {
        try{
            Profile existingProfile = profileRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));

            updateProfileProperties(existingProfile, partialUpdatedProfileDTO);

            Profile updatedProfile = profileRepository.save(existingProfile);

            return ResponseEntity.ok(profileAssembler.toModel(updatedProfile));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        catch (Exception e) {
            // Handle any potential exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the profile.");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteOne(@PathVariable String id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));
        String userId = profile.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
        user.setProfileId(null);
        userRepository.save(user);
        profileRepository.deleteById(id);
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
    private void updateProfileProperties(Profile existingProfile, ProfileDTO updatedProfileDTO) {
        if (updatedProfileDTO.getName() != null && !updatedProfileDTO.getName().isEmpty()) {
            existingProfile.setName(updatedProfileDTO.getName());
        }
        if (updatedProfileDTO.getAbout() != null && !updatedProfileDTO.getAbout().isEmpty()) {
            existingProfile.setAbout(updatedProfileDTO.getAbout());
        }
        if (updatedProfileDTO.getDob_day() != null && !updatedProfileDTO.getDob_day().isEmpty()) {
            existingProfile.setDob_day(updatedProfileDTO.getDob_day());
        }
        if (updatedProfileDTO.getDob_month() != null && !updatedProfileDTO.getDob_month().isEmpty()) {
            existingProfile.setDob_month(updatedProfileDTO.getDob_month());
        }
        if (updatedProfileDTO.getDob_year() != null && !updatedProfileDTO.getDob_year().isEmpty()) {
            existingProfile.setDob_year(updatedProfileDTO.getDob_year());
        }
        if (updatedProfileDTO.getSchool() != null && !updatedProfileDTO.getSchool().isEmpty()) {
            existingProfile.setSchool(updatedProfileDTO.getSchool());
        }
        if (updatedProfileDTO.getStrength_subjects() != null && updatedProfileDTO.getStrength_subjects().length > 0) {
            existingProfile.setStrength_subjects(updatedProfileDTO.getStrength_subjects());
        }
        if (updatedProfileDTO.getWeak_subjects() != null && updatedProfileDTO.getWeak_subjects().length > 0) {
            existingProfile.setWeak_subjects(updatedProfileDTO.getWeak_subjects());
        }
    }

}

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
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileAssembler profileAssembler;

    @GetMapping("")
    public ResponseEntity<?> all() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable String id) {
        return null;
    }

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createOne(@Valid @RequestBody ProfileDTO profileDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("user", "id", userPrincipal.getId()));

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
        return ResponseEntity.internalServerError().body("authentication instanceof AnonymousAuthenticationToken");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putOne(@PathVariable String id) {
        return null;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchOne(@PathVariable String id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable String id) {
        return null;
    }
}

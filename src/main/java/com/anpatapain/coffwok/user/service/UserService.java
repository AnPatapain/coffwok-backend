package com.anpatapain.coffwok.user.service;

import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.plan.model.Plan;
import com.anpatapain.coffwok.plan.repository.PlanRepository;
import com.anpatapain.coffwok.plan.service.PlanService;
import com.anpatapain.coffwok.profile.model.Profile;
import com.anpatapain.coffwok.profile.repository.ProfileRepository;
import com.anpatapain.coffwok.profile.service.ProfileService;
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

    private PlanService planService;

    private ProfileService profileService;

    @Autowired
    public UserService(UserRepository userRepository,
                       ProfileService profileService,
                       PlanService planService) {
        this.userRepository = userRepository;
        this.profileService =profileService;
        this.planService = planService;
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

    public User getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return user;
    }

    public void deleteUser(String id){
        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("user","id",id));

        planService.deletePlan(user.getProfileId());
        profileService.deleteProfile(user.getPlanId());
        userRepository.deleteById(id);
    }
}

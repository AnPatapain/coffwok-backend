package com.anpatapain.coffwok.profile.service;

import com.anpatapain.coffwok.chat.model.ChatRoom;
import com.anpatapain.coffwok.chat.service.ChatService;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.image_upload.exception.ImageUploadException;
import com.anpatapain.coffwok.image_upload.service.ImageStorageService;
import com.anpatapain.coffwok.plan.exception.NoPlanException;
import com.anpatapain.coffwok.plan.model.Plan;
import com.anpatapain.coffwok.plan.repository.PlanRepository;
import com.anpatapain.coffwok.profile.dto.ProfileInfoDTO;
import com.anpatapain.coffwok.profile.model.Profile;
import com.anpatapain.coffwok.profile.model.ProfileAssembler;
import com.anpatapain.coffwok.profile.repository.ProfileRepository;
import com.anpatapain.coffwok.user.repository.UserRepository;
import com.anpatapain.coffwok.user.model.User;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {
    private ProfileRepository profileRepository;
    private UserRepository userRepository;

    private ProfileAssembler profileAssembler;

    private ImageStorageService imageStorageService;

    private PlanRepository planRepository;

    private ChatService chatService;

    private Validator validator;

    private Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository,
                              UserRepository userRepository,
                              ProfileAssembler profileAssembler,
                              ImageStorageService imageStorageService,
                              Validator validator,
                              PlanRepository planRepository,
                              ChatService chatService) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.profileAssembler = profileAssembler;
        this.imageStorageService = imageStorageService;
        this.validator = validator;
        this.planRepository = planRepository;
        this.chatService = chatService;
    }


    @Override
    public List<EntityModel<Profile>> getAll() {
        List<EntityModel<Profile>> profileEntities = profileRepository.findAll()
                .stream()
                .map(profileAssembler::toModel)
                .toList();
        return profileEntities;
    }

    @Override
    public EntityModel<Profile> getOne(String id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));
        return profileAssembler.toModel(profile);
    }

    @Override
    public EntityModel<Profile> createProfile(User user, ProfileInfoDTO profileInfoDTO) {
        Profile profile = new Profile(
                profileInfoDTO.getName(),
                profileInfoDTO.getAbout(),
                profileInfoDTO.getDob(),
                profileInfoDTO.getSchool(),
                profileInfoDTO.getStrength_subjects(),
                profileInfoDTO.getWeak_subjects()
        );
        if (profileInfoDTO.getGender() != null && !profileInfoDTO.getGender().isEmpty()) {
            profile.setGender(profileInfoDTO.getGender());
        }

        profile.setUserId(user.getId());
        profile = profileRepository.save(profile);

        user.setProfileId(profile.getId());
        userRepository.save(user);
        return profileAssembler.toModel(profile);
    }

    @Override
    public EntityModel<Profile> uploadImage(String profileId, MultipartFile imageFile) {
        // Update Profile
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("profile", "id", profileId));

        String imageUrl = "";
        try {
            imageUrl = imageStorageService.saveToCloudinary(imageFile);
            logger.info("cloudinary url: " + imageUrl);
        } catch (ImageUploadException e) {
            String message = "Could not upload " + imageFile.getOriginalFilename();
            logger.error(message);
            throw e;
        }
        profile.setImgUrl(imageUrl);
        profile = profileRepository.save(profile);

        // Update Plan that is associated to Profile
        String userId = profile.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
        String planId = user.getPlanId();
        if (planId != null) {
            Plan plan = planRepository.findPlanById(planId)
                    .orElseThrow(() -> new ResourceNotFoundException("plan", "id", planId));
            plan.setImgUrl(imageUrl);
            planRepository.save(plan);
        }

        //Update ChatRoom that profile is belong to
        chatService.updateProfileForCurrentUser(user, profile);

        return profileAssembler.toModel(profile);
    }

    @Override
    public void deleteProfile(String id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));
        String userId = profile.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
        user.setProfileId(null);
        userRepository.save(user);
        profileRepository.deleteById(id);
    }

    @Override
    public EntityModel<Profile> patchProfile(String id, ProfileInfoDTO partialUpdatedProfileInfoDTO) {
        // Update Profile
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));

        updateProfileProperties(existingProfile, partialUpdatedProfileInfoDTO);
        Profile updatedProfile = profileRepository.save(existingProfile);

        // Update Plan that associates to Profile
        String userId = updatedProfile.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));

        String planId = user.getPlanId();
        Plan existingPlan = planRepository.findPlanById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("plan", "id", planId));

        existingPlan.setName(updatedProfile.getName());
        existingPlan.setSchool(updatedProfile.getSchool());
        existingPlan.setStrength_subjects(updatedProfile.getStrength_subjects());
        existingPlan.setWeak_subjects(updatedProfile.getWeak_subjects());
        planRepository.save(existingPlan);

        // Update all chatroom that profile is belong to
        chatService.updateProfileForCurrentUser(user, updatedProfile);

        return profileAssembler.toModel(updatedProfile);
    }

    @Override
    public EntityModel<Profile> putProfile(String id, ProfileInfoDTO updatedProfileInfoDTO) {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));

        updateProfileProperties(existingProfile, updatedProfileInfoDTO);

        Profile updatedProfile = profileRepository.save(existingProfile);

        return profileAssembler.toModel(updatedProfile);

    }


    private void updateProfileProperties(Profile existingProfile, ProfileInfoDTO updatedProfileInfoDTO) {
        if (updatedProfileInfoDTO.getName() != null && !updatedProfileInfoDTO.getName().isEmpty()) {
            existingProfile.setName(updatedProfileInfoDTO.getName());
        }
        if (updatedProfileInfoDTO.getAbout() != null && !updatedProfileInfoDTO.getAbout().isEmpty()) {
            existingProfile.setAbout(updatedProfileInfoDTO.getAbout());
        }
        if (updatedProfileInfoDTO.getDob() != null && !updatedProfileInfoDTO.getDob().isEmpty()) {
            existingProfile.setDob(updatedProfileInfoDTO.getDob());
        }
        if (updatedProfileInfoDTO.getSchool() != null && !updatedProfileInfoDTO.getSchool().isEmpty()) {
            existingProfile.setSchool(updatedProfileInfoDTO.getSchool());
        }
        if (updatedProfileInfoDTO.getGender() != null && !updatedProfileInfoDTO.getGender().isEmpty()) {
            existingProfile.setGender(updatedProfileInfoDTO.getGender());
        }
        if (updatedProfileInfoDTO.getStrength_subjects() != null && updatedProfileInfoDTO.getStrength_subjects().length > 0) {
            existingProfile.setStrength_subjects(updatedProfileInfoDTO.getStrength_subjects());
        }
        if (updatedProfileInfoDTO.getWeak_subjects() != null && updatedProfileInfoDTO.getWeak_subjects().length > 0) {
            existingProfile.setWeak_subjects(updatedProfileInfoDTO.getWeak_subjects());
        }
    }
}

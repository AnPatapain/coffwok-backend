package com.anpatapain.coffwok.profile.service;

import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.image_upload.exception.ImageUploadException;
import com.anpatapain.coffwok.image_upload.service.ImageStorageService;
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
public class ProfileServiceImpl implements ProfileService{
    private ProfileRepository profileRepository;
    private UserRepository userRepository;

    private ProfileAssembler profileAssembler;

    private ImageStorageService imageStorageService;

    private Validator validator;

    private Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository,
                              UserRepository userRepository,
                              ProfileAssembler profileAssembler,
                              ImageStorageService imageStorageService,
                              Validator validator) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.profileAssembler = profileAssembler;
        this.imageStorageService = imageStorageService;
        this.validator = validator;
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
                profileInfoDTO.getDob_day(),
                profileInfoDTO.getDob_month(),
                profileInfoDTO.getDob_year(),
                profileInfoDTO.getSchool(),
                profileInfoDTO.getStrength_subjects(),
                profileInfoDTO.getWeak_subjects()
        );

        profile.setUserId(user.getId());
        profile = profileRepository.save(profile);

        user.setProfileId(profile.getId());
        userRepository.save(user);
        return profileAssembler.toModel(profile);
    }
    @Override
    public EntityModel<Profile> uploadImage(String profileId, MultipartFile imageFile) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("profile", "id", profileId));

        String imageUrl = "";
        try {
            imageUrl = imageStorageService.saveToCloudinary(imageFile);
            logger.info("cloudinary url: " + imageUrl);
        }catch (ImageUploadException e) {
            String message = "Could not upload " + imageFile.getOriginalFilename();
            logger.error(message);
            throw e;
        }

        profile.setImgUrl(imageUrl);
        profile = profileRepository.save(profile);

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
        try{
            Profile existingProfile = profileRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));

            updateProfileProperties(existingProfile, partialUpdatedProfileInfoDTO);

            Profile updatedProfile = profileRepository.save(existingProfile);

            return profileAssembler.toModel(updatedProfile);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            // Handle any potential exceptions
            logger.error("An error occurred while updating the profile (patch)");
            throw e;
        }
    }

    @Override
    public EntityModel<Profile> putProfile(String id, ProfileInfoDTO updatedProfileInfoDTO) {
        try{
            Profile existingProfile = profileRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));

            updateProfileProperties(existingProfile, updatedProfileInfoDTO);

            Profile updatedProfile = profileRepository.save(existingProfile);

            return profileAssembler.toModel(updatedProfile);
        } catch (Exception e) {
            // Handle any potential exceptions
            logger.error("An error occurred while updating the profile (put)");
            throw e;
        }
    }


    private void updateProfileProperties(Profile existingProfile, ProfileInfoDTO updatedProfileInfoDTO) {
        if (updatedProfileInfoDTO.getName() != null && !updatedProfileInfoDTO.getName().isEmpty()) {
            existingProfile.setName(updatedProfileInfoDTO.getName());
        }
        if (updatedProfileInfoDTO.getAbout() != null && !updatedProfileInfoDTO.getAbout().isEmpty()) {
            existingProfile.setAbout(updatedProfileInfoDTO.getAbout());
        }
        if (updatedProfileInfoDTO.getDob_day() != null && !updatedProfileInfoDTO.getDob_day().isEmpty()) {
            existingProfile.setDob_day(updatedProfileInfoDTO.getDob_day());
        }
        if (updatedProfileInfoDTO.getDob_month() != null && !updatedProfileInfoDTO.getDob_month().isEmpty()) {
            existingProfile.setDob_month(updatedProfileInfoDTO.getDob_month());
        }
        if (updatedProfileInfoDTO.getDob_year() != null && !updatedProfileInfoDTO.getDob_year().isEmpty()) {
            existingProfile.setDob_year(updatedProfileInfoDTO.getDob_year());
        }
        if (updatedProfileInfoDTO.getSchool() != null && !updatedProfileInfoDTO.getSchool().isEmpty()) {
            existingProfile.setSchool(updatedProfileInfoDTO.getSchool());
        }
        if (updatedProfileInfoDTO.getStrength_subjects() != null && updatedProfileInfoDTO.getStrength_subjects().length > 0) {
            existingProfile.setStrength_subjects(updatedProfileInfoDTO.getStrength_subjects());
        }
        if (updatedProfileInfoDTO.getWeak_subjects() != null && updatedProfileInfoDTO.getWeak_subjects().length > 0) {
            existingProfile.setWeak_subjects(updatedProfileInfoDTO.getWeak_subjects());
        }
    }
}

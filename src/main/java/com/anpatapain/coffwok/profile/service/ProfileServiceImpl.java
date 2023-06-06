package com.anpatapain.coffwok.profile.service;

import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.image_upload.exception.ImageUploadException;
import com.anpatapain.coffwok.image_upload.service.ImageStorageService;
import com.anpatapain.coffwok.profile.dto.ProfileDTO;
import com.anpatapain.coffwok.profile.model.Profile;
import com.anpatapain.coffwok.profile.model.ProfileAssembler;
import com.anpatapain.coffwok.profile.repository.ProfileRepository;
import com.anpatapain.coffwok.user.repository.UserRepository;
import com.anpatapain.coffwok.user.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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
    public EntityModel<Profile> createProfile(User user, ProfileDTO profileDTO) {
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
        userRepository.save(user);
        return profileAssembler.toModel(profile);
    }

    @Override
    public EntityModel<Profile> createProfileWithImage(User user, ProfileDTO profileDTO, MultipartFile imageFile) {
        Set<ConstraintViolation<ProfileDTO>> violations = validator.validate(profileDTO);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        String imageUrl = "";
        try {
            imageUrl = imageStorageService.saveToCloudinary(imageFile);
            logger.info("cloudinary url: " + imageUrl);
        }catch (ImageUploadException e) {
            String message = "Could not upload " + imageFile.getOriginalFilename();
            logger.error(message);
            throw e;
        }

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
        profile.setImgUrl(imageUrl);
        profile.setUserId(user.getId());
        profile = profileRepository.save(profile);
        user.setProfileId(profile.getId());
        userRepository.save(user);
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
    public EntityModel<Profile> patchProfile(String id, ProfileDTO partialUpdatedProfileDTO) {
        try{
            Profile existingProfile = profileRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));

            updateProfileProperties(existingProfile, partialUpdatedProfileDTO);

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
    public EntityModel<Profile> putProfile(String id, ProfileDTO updatedProfileDTO) {
        try{
            Profile existingProfile = profileRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));

            updateProfileProperties(existingProfile, updatedProfileDTO);

            Profile updatedProfile = profileRepository.save(existingProfile);

            return profileAssembler.toModel(updatedProfile);
        } catch (Exception e) {
            // Handle any potential exceptions
            logger.error("An error occurred while updating the profile (put)");
            throw e;
        }
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

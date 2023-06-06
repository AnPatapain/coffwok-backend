package com.anpatapain.coffwok.profile;

import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.user.UserRepository;
import com.anpatapain.coffwok.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    private ProfileRepository profileRepository;
    private UserRepository userRepository;

    private ProfileAssembler profileAssembler;

    private Logger logger = LoggerFactory.getLogger(ProfileService.class);

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, ProfileAssembler profileAssembler) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.profileAssembler = profileAssembler;
    }

    public List<EntityModel<Profile>> getAll() {
        List<EntityModel<Profile>> profileEntities = profileRepository.findAll()
                .stream()
                .map(profileAssembler::toModel)
                .toList();
        return profileEntities;
    }

    public EntityModel<Profile> getOne(String id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("profile", "id", id));
        return profileAssembler.toModel(profile);
    }

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

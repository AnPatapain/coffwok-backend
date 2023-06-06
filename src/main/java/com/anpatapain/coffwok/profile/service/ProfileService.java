package com.anpatapain.coffwok.profile.service;

import com.anpatapain.coffwok.profile.dto.ProfileDTO;
import com.anpatapain.coffwok.profile.model.Profile;
import com.anpatapain.coffwok.user.model.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfileService {
    public List<EntityModel<Profile>> getAll();
    public EntityModel<Profile> getOne(String id);
    public EntityModel<Profile> createProfile(User user, ProfileDTO profileDTO);
    public EntityModel<Profile> createProfileWithImage(User user, ProfileDTO profileDTO, MultipartFile imageFile);
    public void deleteProfile(String id);
    public EntityModel<Profile> patchProfile(String id, ProfileDTO partialUpdatedProfileDTO);
    public EntityModel<Profile> putProfile(String id, ProfileDTO updatedProfileDTO);
}

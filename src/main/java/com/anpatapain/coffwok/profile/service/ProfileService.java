package com.anpatapain.coffwok.profile.service;

import com.anpatapain.coffwok.profile.dto.ProfileInfoDTO;
import com.anpatapain.coffwok.profile.model.Profile;
import com.anpatapain.coffwok.user.model.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfileService {
    public List<EntityModel<Profile>> getAll(int page, int pageSize);
    public EntityModel<Profile> getOne(String id);
    public EntityModel<Profile> createProfile(User user, ProfileInfoDTO profileDTO);
    public void deleteProfile(String id);
    public EntityModel<Profile> patchProfile(String id, ProfileInfoDTO partialUpdatedProfileDTO);
    public EntityModel<Profile> putProfile(String id, ProfileInfoDTO updatedProfileDTO);
    public EntityModel<Profile> uploadImage(String profileId, MultipartFile imageFile);
}

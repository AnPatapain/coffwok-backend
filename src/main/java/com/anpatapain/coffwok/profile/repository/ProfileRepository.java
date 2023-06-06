package com.anpatapain.coffwok.profile.repository;

import com.anpatapain.coffwok.profile.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, String> {
    Optional<Profile> findById(String id);
}

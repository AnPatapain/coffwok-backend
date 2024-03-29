package com.anpatapain.coffwok.user.repository;

import com.anpatapain.coffwok.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}

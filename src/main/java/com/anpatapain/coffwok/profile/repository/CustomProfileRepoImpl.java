package com.anpatapain.coffwok.profile.repository;

import com.anpatapain.coffwok.plan.model.Plan;
import com.anpatapain.coffwok.profile.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public class CustomProfileRepoImpl implements CustomProfileRepo{
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomProfileRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<Profile> getAll(int page, int size) {
        Query query = new Query();
        query.with(PageRequest.of(page, size));
        return this.mongoTemplate.find(query, Profile.class);
    }
}

package com.anpatapain.coffwok.plan.repository;

import com.anpatapain.coffwok.plan.model.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomPlanRepoImpl implements CustomPlanRepo{
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomPlanRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Plan> getAll(int pageNumber, int pageSize) {
        Query query = new Query();
        query.with(PageRequest.of(pageNumber, pageSize));
        return this.mongoTemplate.find(query, Plan.class);
    }
}

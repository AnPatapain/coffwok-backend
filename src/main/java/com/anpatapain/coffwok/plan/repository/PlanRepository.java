package com.anpatapain.coffwok.plan.repository;

import com.anpatapain.coffwok.plan.model.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PlanRepository extends MongoRepository<Plan, String> {
    Optional<Plan> findPlanById(String id);
}

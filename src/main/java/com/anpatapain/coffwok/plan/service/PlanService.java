package com.anpatapain.coffwok.plan.service;

import com.anpatapain.coffwok.plan.dto.PlanDto;
import com.anpatapain.coffwok.plan.model.Plan;
import com.anpatapain.coffwok.user.model.User;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public interface PlanService {
    public List<EntityModel<Plan>> getAll();
    public EntityModel<Plan> getOne(String id);
    public EntityModel<Plan> createPlan(User user, PlanDto planDto);
    public void deletePlan(String id);
}
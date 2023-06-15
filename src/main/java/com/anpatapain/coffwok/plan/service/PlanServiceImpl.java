package com.anpatapain.coffwok.plan.service;

import com.anpatapain.coffwok.plan.dto.PlanDto;
import com.anpatapain.coffwok.plan.model.Plan;
import com.anpatapain.coffwok.plan.model.PlanAssembler;
import com.anpatapain.coffwok.plan.repository.PlanRepository;
import com.anpatapain.coffwok.user.model.User;
import com.anpatapain.coffwok.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService{
    private Logger logger = LoggerFactory.getLogger(PlanServiceImpl.class);
    private UserRepository userRepository;
    private PlanAssembler planAssembler;
    private PlanRepository planRepository;

    @Autowired
    public PlanServiceImpl(UserRepository userRepository,
                           PlanAssembler planAssembler,
                           PlanRepository planRepository) {
        this.userRepository = userRepository;
        this.planAssembler = planAssembler;
        this.planRepository = planRepository;
    }

    @Override
    public List<EntityModel<Plan>> getAll() {
        //TODO: Parker
        return null;
    }

    @Override
    public EntityModel<Plan> getOne(String id) {
        //TODO: Parker
        return null;
    }

    @Override
    public EntityModel<Plan> createPlan(User user, PlanDto planDto) {
        Plan plan = new Plan(
                planDto.getCoffeeShop(),
                planDto.getSchedule()
        );

        plan.setUserId(user.getId());
        plan = planRepository.save(plan);

        user.setPlanId(plan.getId());
        userRepository.save(user);
        return planAssembler.toModel(plan);
    }

    @Override
    public void deletePlan(String id) {
        //TODO: Parker
    }
}

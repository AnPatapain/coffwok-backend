package com.anpatapain.coffwok.plan.service;

import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
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
        List<EntityModel<Plan>> planEntities = this.planRepository.findAll()
                .stream()
                .map(this.planAssembler::toModel)
                .toList();
        return planEntities;
    }

    @Override
    public EntityModel<Plan> getOne(String id) {
        Plan plan = planRepository.findPlanById(id).orElseThrow(()->new ResourceNotFoundException("plan","id",id));

        return planAssembler.toModel(plan);
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
        Plan plan = planRepository.findPlanById(id).orElseThrow(()->new ResourceNotFoundException("plan","id",id));
        String userid = plan.getUserId();
        User user = userRepository.findById(userid).orElseThrow(()-> new ResourceNotFoundException("user","id",userid));
        user.setPlanId(null);
        userRepository.save(user);
        planRepository.deleteById(id);
    }
}

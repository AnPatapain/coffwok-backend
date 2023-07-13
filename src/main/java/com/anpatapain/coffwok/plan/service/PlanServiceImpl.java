package com.anpatapain.coffwok.plan.service;

import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.plan.dto.PlanDto;
import com.anpatapain.coffwok.plan.model.Plan;
import com.anpatapain.coffwok.plan.model.PlanAssembler;
import com.anpatapain.coffwok.plan.repository.CustomPlanRepo;
import com.anpatapain.coffwok.plan.repository.PlanRepository;
import com.anpatapain.coffwok.profile.model.Profile;
import com.anpatapain.coffwok.profile.repository.ProfileRepository;
import com.anpatapain.coffwok.user.model.User;
import com.anpatapain.coffwok.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl implements PlanService{
    private Logger logger = LoggerFactory.getLogger(PlanServiceImpl.class);
    private UserRepository userRepository;
    private ProfileRepository profileRepository;
    private PlanAssembler planAssembler;
    private PlanRepository planRepository;

    private CustomPlanRepo customPlanRepo;

    @Autowired
    public PlanServiceImpl(UserRepository userRepository,
                           PlanAssembler planAssembler,
                           ProfileRepository profileRepository,
                           PlanRepository planRepository,
                           CustomPlanRepo customPlanRepo) {
        this.userRepository = userRepository;
        this.planAssembler = planAssembler;
        this.planRepository = planRepository;
        this.profileRepository = profileRepository;
        this.customPlanRepo = customPlanRepo;
    }

    @Override
    public List<EntityModel<Plan>> getAll(int pageNumber, int pageSize) {
        List<EntityModel<Plan>> planEntities = this.customPlanRepo.getAll(pageNumber, pageSize)
                .stream()
                .map(this.planAssembler::toModel)
                .collect(Collectors.toList());
        Collections.shuffle(planEntities);
        return planEntities;
    }

    @Override
    public EntityModel<Plan> getOne(String id) throws ResourceNotFoundException{
        Plan plan = planRepository.findPlanById(id).orElseThrow(()->new ResourceNotFoundException("plan","id",id));
        return planAssembler.toModel(plan);
    }

    @Override
    public EntityModel<Plan> createPlan(User user, PlanDto planDto) throws ResourceNotFoundException{
        if(user.getProfileId() != null) {
            Profile profile = profileRepository.findById(user.getProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("profile", "id", user.getProfileId()));

            Plan plan = new Plan(
                    planDto.getCoffeeShop(),
                    planDto.getSchedule(),
                    planDto.getPlanDetails()
            );

            plan.setName(profile.getName());
            plan.setSchool(profile.getSchool());
            plan.setImgUrl(profile.getImgUrl());
            plan.setStrength_subjects(profile.getStrength_subjects());
            plan.setWeak_subjects(profile.getWeak_subjects());
            plan.setAbout(profile.getAbout());

            plan.setUserId(user.getId());
            plan = planRepository.save(plan);

            user.setPlanId(plan.getId());
            userRepository.save(user);
            return planAssembler.toModel(plan);
        }
        return null;
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

    @Override
    public EntityModel<Plan> patchPlan(String id,PlanDto partialUpdatePlanDto){
        Plan existingPlan = planRepository.findPlanById(id)
                .orElseThrow(()->new ResourceNotFoundException("plan","id",id));
        updatePlanProperties(existingPlan,partialUpdatePlanDto);
        Plan updatedPlan = planRepository.save(existingPlan);
        return planAssembler.toModel(updatedPlan);
    }

    @Override
    public EntityModel<Plan> putPlan(String id, PlanDto updatePlanDto){
        Plan existingPlan = planRepository.findPlanById(id)
                .orElseThrow(()->new ResourceNotFoundException("plan","id",id));
        updatePlanProperties(existingPlan,updatePlanDto);
        Plan updatedPlan = planRepository.save(existingPlan);
        return planAssembler.toModel(updatedPlan);
    }

    private void updatePlanProperties(Plan existingPlan, PlanDto updatePlanDTO){
        if(updatePlanDTO.getSchedule()!= null ){
            existingPlan.setSchedule(updatePlanDTO.getSchedule());
        }
        if(updatePlanDTO.getCoffeeShop()!= null && !updatePlanDTO.getCoffeeShop().isEmpty() ){
            existingPlan.setCoffeeShop(updatePlanDTO.getCoffeeShop());
        }
        if(updatePlanDTO.getPlanDetails()!= null && !updatePlanDTO.getPlanDetails().isEmpty() ){
            existingPlan.setPlanDetails(updatePlanDTO.getPlanDetails());
        }
    }
}

package com.anpatapain.coffwok.plan.controller;

import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.common.payload.response.ApiResponse;
import com.anpatapain.coffwok.plan.dto.PlanDto;
import com.anpatapain.coffwok.plan.model.Plan;
import com.anpatapain.coffwok.plan.service.PlanService;
import com.anpatapain.coffwok.user.model.User;
import com.anpatapain.coffwok.user.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/plans")
public class PlanController {
    private Logger logger = LoggerFactory.getLogger(PlanController.class);
    private PlanService planService;
    private UserService userService;

    @Autowired
    public PlanController(PlanService planService, UserService userService) {
        this.planService = planService;
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentPlan() {
        User user;
        try{
            user = userService.getCurrentAuthenticatedUser();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }
        if(user.getPlanId() == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("plan not found for user");
        }

        try {
            EntityModel<Plan> planEntityModel = planService.getOne(user.getPlanId());
            return ResponseEntity.ok(planEntityModel);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public CollectionModel<EntityModel<Plan>> all(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "2") int size) {
        List<EntityModel<Plan>> planEntities = planService.getAll(page, size);
        return CollectionModel.of(planEntities, linkTo(methodOn(PlanController.class).all(0, 2)).withRel("plans"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> one(@PathVariable String id) {
        try{
            EntityModel<Plan> planEntity = planService.getOne(id);
            return ResponseEntity.ok(planEntity);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }

    }

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPlan(@Valid @RequestBody PlanDto planDto) {
        User user;
        try{
            user = userService.getCurrentAuthenticatedUser();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }

        if(user.getPlanId() == null && user.getProfileId() != null) {
            EntityModel<Plan> planEntityModel = planService.createPlan(user, planDto);
            return ResponseEntity.ok(planEntityModel);
        }else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User has already plan or user has not created profile"));
        }
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity deleteOne(@PathVariable String id){
        User user;
        try{
            user = userService.getCurrentAuthenticatedUser();
            if(user.getPlanId()==null || !user.getPlanId().equals(id)){
                return  ResponseEntity.badRequest().body("you are not the owner of this plan");
            }
        }catch (ResourceNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage()+"user not found");
        }

        planService.deletePlan(id);
        return ResponseEntity.ok("Plan deleted successfully");
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> putOne(@PathVariable String id,@Valid @RequestBody PlanDto updatePlanDto){
        User user;
        try{
            user = userService.getCurrentAuthenticatedUser();
            if(user.getPlanId() == null || !user.getPlanId().equals(id)) {
                return ResponseEntity.badRequest().body("you are not the owner of this plan");
            }
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }

        EntityModel<Plan> updatePlanEntity;
        try{
            updatePlanEntity = planService.putPlan(id,updatePlanDto);
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
        return ResponseEntity.ok(updatePlanEntity);
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> patchOne(@PathVariable String id,@RequestBody PlanDto partialUpdatePlanDto){
        User user;
        try{
            user = userService.getCurrentAuthenticatedUser();
            if(user.getPlanId() == null || !user.getPlanId().equals(id)) {
                return ResponseEntity.badRequest().body("you are not the owner of this plan");
            }
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage() + "user not found");
        }

        EntityModel<Plan> updatePlanEntity;
        try{
            updatePlanEntity = planService.putPlan(id,partialUpdatePlanDto);
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
        return ResponseEntity.ok(updatePlanEntity);
    }
}



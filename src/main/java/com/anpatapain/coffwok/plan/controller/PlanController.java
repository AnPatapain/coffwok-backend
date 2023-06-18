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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public CollectionModel<EntityModel<Plan>> all() {
        //TODO: Parker
        return null;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> one(@PathVariable String id) {
        //TODO: Parker
        return null;
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

        if(user.getPlanId() == null) {
            EntityModel<Plan> planEntityModel = planService.createPlan(user, planDto);
            return ResponseEntity.ok(planEntityModel);
        }else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User has already plan"));
        }
    }

    /**
     * TODO: putOne, patchOne, deleteOne
     *       Parker + Dau
     */

}



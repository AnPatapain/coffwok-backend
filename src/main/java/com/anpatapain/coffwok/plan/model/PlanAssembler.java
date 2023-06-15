package com.anpatapain.coffwok.plan.model;

import com.anpatapain.coffwok.plan.controller.PlanController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PlanAssembler implements RepresentationModelAssembler<Plan, EntityModel<Plan>> {
    @Override
    public EntityModel<Plan> toModel(Plan plan) {
        return EntityModel.of(plan,
                linkTo(methodOn(PlanController.class).one(plan.getId())).withSelfRel(),
                linkTo(methodOn(PlanController.class).all()).withRel("plans")
        );
    }
}

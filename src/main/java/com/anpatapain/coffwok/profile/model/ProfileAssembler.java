package com.anpatapain.coffwok.profile.model;

import com.anpatapain.coffwok.profile.controller.ProfileController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProfileAssembler implements RepresentationModelAssembler<Profile, EntityModel<Profile>> {
    @Override
    public EntityModel<Profile> toModel(Profile profile) {
        return EntityModel.of(profile,
                linkTo(methodOn(ProfileController.class).one(profile.getId())).withSelfRel(),
                linkTo(methodOn(ProfileController.class).all(0, 10)).withRel("profiles")
        );
    }
}

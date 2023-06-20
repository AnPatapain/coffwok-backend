package com.anpatapain.coffwok.plan.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "plans")
@Getter @Setter @NoArgsConstructor
public class Plan {
    @Id
    private String id;

    private String userId;

    private String name;

    private String imgUrl;

    private String school;

    private String[] strength_subjects;

    private String[] weak_subjects;

    @NotBlank
    private String coffeeShop;

    @NotBlank
    private String schedule;


    // Construct Plan by DTO
    public Plan(String coffeeShop, String schedule) {
        this.coffeeShop = coffeeShop;
        this.schedule = schedule;
    }
}

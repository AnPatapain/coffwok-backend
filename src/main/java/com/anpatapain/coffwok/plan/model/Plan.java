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

    @NotBlank
    private String coffeeShop;

    @NotBlank
    private LocalDateTime schedule;

    public Plan(String coffeeShop, LocalDateTime schedule) {
        this.coffeeShop = coffeeShop;
        this.schedule = schedule;
    }
}

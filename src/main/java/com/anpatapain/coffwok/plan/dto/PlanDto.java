package com.anpatapain.coffwok.plan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Data
public class PlanDto {
    @NotBlank
    private String coffeeShop;

    @NotNull
    private LocalDateTime schedule;
}

package com.anpatapain.coffwok.profile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Data
public class ProfileInfoDTO {
    @NotBlank
    @Size(max = 50, message = "name must not be too long maximum 50 chars")
    private String name;

    @NotBlank
    @Size(max = 200)
    private String about;

    @NotBlank
    private String school;

    private String gender;


    private String[] strength_subjects;

    private String[] weak_subjects;
}

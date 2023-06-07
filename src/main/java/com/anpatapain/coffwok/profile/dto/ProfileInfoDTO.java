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
    private String dob_day;

    @NotBlank
    private String dob_month;

    @NotBlank
    private String dob_year;

    @NotBlank
    private String school;


    private String[] strength_subjects;

    private String[] weak_subjects;
}

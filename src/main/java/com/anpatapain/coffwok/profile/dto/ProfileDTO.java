package com.anpatapain.coffwok.profile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Data
public class ProfileDTO {
    @NotBlank
    @Size(max = 50, message = "name must not be too long maximum 50 chars")
    private String name;

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

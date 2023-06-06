package com.anpatapain.coffwok.profile.model;

import com.anpatapain.coffwok.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "profiles")
@Getter @Setter @NoArgsConstructor
public class Profile {
    @Id
    private String id;

    private String userId;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 200)
    private String about;

    @NotBlank
    private String imgUrl;

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

    public Profile(String name, String about,
                   String dob_day, String dob_month, String dob_year, String school,
                   String[] strength_subjects, String[] weak_subjects) {
        this.name = name;
        this.about = about;
        this.dob_day = dob_day;
        this.dob_month = dob_month;
        this.dob_year = dob_year;
        this.school = school;
        this.strength_subjects = strength_subjects;
        this.weak_subjects = weak_subjects;
    }
}

package com.anpatapain.coffwok.profile.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
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

    private String gender;

    @NotBlank
    private String school;


    private String[] strength_subjects;

    private String[] weak_subjects;

    public Profile(String name, String about, String school,
                   String[] strength_subjects, String[] weak_subjects) {
        this.name = name;
        this.about = about;
        this.school = school;
        this.strength_subjects = strength_subjects;
        this.weak_subjects = weak_subjects;
    }
}

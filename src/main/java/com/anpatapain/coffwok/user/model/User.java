package com.anpatapain.coffwok.user.model;

import com.anpatapain.coffwok.profile.Profile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter @Setter @NoArgsConstructor
public class User {
    @Id
    private String id;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max=40)
    private String password;

    private Role role;

    @NotBlank
    private AuthProvider provider;

    private String providerId;

    private String profileId;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

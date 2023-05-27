package com.anpatapain.coffwok.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

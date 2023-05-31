package com.anpatapain.coffwok.common.payload.request;

import com.anpatapain.coffwok.user.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupRequest {
    @NotBlank
    @Size(max = 50) @Email
    private String email;

    @NotBlank @Size(min = 6, max = 40)
    private String password;

    private Role role;
}

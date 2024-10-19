package com.example.shemtong.domain.auth.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignupRequest (

        @NotNull
        String username,

        @NotNull
        String password,

        @Email
        String email
) {

}

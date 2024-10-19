package com.example.shemtong.domain.auth.dto.login;

import jakarta.validation.constraints.NotNull;

public record LoginRequest (
        @NotNull
        String username,

        @NotNull
        String password
) {
    
}

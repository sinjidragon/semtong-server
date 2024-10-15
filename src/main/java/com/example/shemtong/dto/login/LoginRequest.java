package com.example.shemtong.dto.login;

import jakarta.validation.constraints.NotNull;

public record LoginRequest (
        @NotNull
        String username,

        @NotNull
        String password
) {
    
}

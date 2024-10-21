package com.example.shemtong.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RefreshResponse {

    private String accessToken;

    private String refreshToken;

    private String tokenType;
}

package com.example.shemtong.domain.auth.dto.refresh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RefreshResponse {

    private String userRole;

    private String accessToken;

    private String refreshToken;

    private String tokenType;
}

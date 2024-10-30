package com.example.shemtong.domain.user.dto;

import com.example.shemtong.domain.user.Entity.UserEntity;
import com.example.shemtong.domain.user.Entity.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private Long groupid;

    public static UserResponse fromUserEntity(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getUid())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .role(userEntity.getRole())
                .groupid(userEntity.getGroup().getId())
                .build();
    }
}

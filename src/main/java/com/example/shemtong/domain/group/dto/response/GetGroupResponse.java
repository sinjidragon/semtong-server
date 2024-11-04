package com.example.shemtong.domain.group.dto.response;

import com.example.shemtong.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetGroupResponse {
    private String groupName;
    private String groupCode;
    private List<UserEntity> members;
}

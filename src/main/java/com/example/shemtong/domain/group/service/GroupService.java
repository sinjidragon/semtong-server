package com.example.shemtong.domain.group.service;

import com.example.shemtong.domain.group.dto.request.JoinGroupRequest;
import com.example.shemtong.domain.group.dto.response.CreateGroupResponse;
import com.example.shemtong.domain.group.dto.response.JoinGroupResponse;
import com.example.shemtong.domain.group.entity.GroupEntity;
import com.example.shemtong.domain.group.repository.GroupRepository;
import com.example.shemtong.domain.user.Entity.UserEntity;
import com.example.shemtong.domain.user.Entity.UserRole;
import com.example.shemtong.domain.user.repository.UserRepository;
import com.example.shemtong.global.dto.ErrorResponse;
import com.example.shemtong.global.dto.SuccessResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;


@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;

    }

    public ResponseEntity<?> createGroup(Principal principal) {

        UserEntity user = userRepository.findById(Long.parseLong(principal.getName()))
            .orElseThrow(() -> new RuntimeException("User not found"));

        String groupCode;
        boolean isUnique = false;

        do {
            groupCode = RandomStringUtils.randomAlphabetic(6).toUpperCase();
            isUnique = !groupRepository.findByGroupcode(groupCode).isPresent();  // 그룹 코드 중복 체크
        } while (!isUnique);

        GroupEntity group = new GroupEntity().builder()
                .groupname(user.getUsername() +"의 그룹")
                .groupcode(groupCode)
                .build();

        groupRepository.save(group);

        user.setRole(UserRole.valueOf("AGENT"));
        user.setGroup(group);
        userRepository.save(user);

        return ResponseEntity.ok(new CreateGroupResponse(user.getUsername() +"의 그룹", groupCode));
    }

    public ResponseEntity<?> joinGroup(Principal principal,JoinGroupRequest joinGroupRequest) {
        GroupEntity group = groupRepository.findByGroupcode(joinGroupRequest.getGroupCode()).orElseThrow(null);

        UserEntity user = userRepository.findById(Long.parseLong(principal.getName()))
                .orElseThrow(null);

        if (group == null){
            return ResponseEntity.badRequest().body(new ErrorResponse("joinGroup failed", "그룹을 찾을 수 없습니다."));
        }
        if (user == null){
            return ResponseEntity.badRequest().body(new ErrorResponse("joinGroup failed", "유저를 찾을 수 없습니다."));
        }
        if (user.getRole() != null){
            return ResponseEntity.badRequest().body(new ErrorResponse("joinGroup failed", "이미 그룹에 참가한 유저입니다."));
        }

        user.setRole(UserRole.valueOf("MEMBER"));
        user.setGroup(group);
        userRepository.save(user);

        return ResponseEntity.ok(new JoinGroupResponse(group.getGroupname()));
    }

}

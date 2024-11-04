package com.example.shemtong.domain.group.service;

import com.example.shemtong.domain.group.dto.request.JoinGroupRequest;
import com.example.shemtong.domain.group.dto.request.RemoveMemberRequest;
import com.example.shemtong.domain.group.dto.response.CreateGroupResponse;
import com.example.shemtong.domain.group.dto.response.GetGroupResponse;
import com.example.shemtong.domain.group.dto.response.JoinGroupResponse;
import com.example.shemtong.domain.group.entity.GroupEntity;
import com.example.shemtong.domain.group.exception.GroupErrorCode;
import com.example.shemtong.domain.group.repository.GroupRepository;
import com.example.shemtong.domain.user.entity.UserEntity;
import com.example.shemtong.domain.user.entity.UserRole;
import com.example.shemtong.domain.user.entity.UserState;
import com.example.shemtong.domain.user.exception.UserErrorCode;
import com.example.shemtong.domain.user.repository.UserRepository;
import com.example.shemtong.global.dto.SuccessResponse;
import com.example.shemtong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public void verifyUser(UserEntity user) {
        if (user == null)
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);

        if (user.getState() == UserState.DELETED)
            throw new CustomException(UserErrorCode.USER_IS_DELETED);
    }

    public void verifyGroup(GroupEntity group) {
        if (group == null)
            throw new CustomException(GroupErrorCode.GROUP_NOT_FOUND);
    }

    public void verifyAgent(UserEntity agent) {
        if (agent == null)
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);

        if (agent.getState() == UserState.DELETED)
            throw new CustomException(UserErrorCode.USER_IS_DELETED);

        if (agent.getRole() != UserRole.AGENT)
            throw new CustomException(UserErrorCode.USER_UNAUTHORIZED);
    }

    public ResponseEntity<CreateGroupResponse> createGroup(Principal principal) {

        UserEntity user = userRepository.findById(Long.parseLong(principal.getName())).orElseThrow(null);
        verifyUser(user);

        String groupCode;
        boolean isUnique = false;

        do {
            groupCode = RandomStringUtils.randomAlphabetic(6).toUpperCase();
            isUnique = !groupRepository.findByGroupcode(groupCode).isPresent();
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

    public ResponseEntity<JoinGroupResponse> joinGroup(Principal principal,JoinGroupRequest joinGroupRequest) {
        GroupEntity group = groupRepository.findByGroupcode(joinGroupRequest.getGroupCode()).orElseThrow(null);

        UserEntity user = userRepository.findById(Long.parseLong(principal.getName()))
                .orElseThrow(null);

        verifyGroup(group);
        verifyUser(user);

        if (user.getGroup() != null){
            throw new CustomException(GroupErrorCode.USER_ALREADY_INGROUP);
        }

        user.setRole(UserRole.valueOf("MEMBER"));
        user.setGroup(group);
        userRepository.save(user);

        return ResponseEntity.ok(new JoinGroupResponse(group.getGroupname()));
    }

    public ResponseEntity<GetGroupResponse> getGroup(Principal principal) {
        UserEntity user = userRepository.findById(Long.parseLong(principal.getName())).orElseThrow(null);

        GroupEntity group = user.getGroup();
        verifyGroup(group);

        return ResponseEntity.ok(new GetGroupResponse(group.getGroupname(), group.getGroupcode(), group.getUsers()));
    }

    public ResponseEntity<SuccessResponse> removeMember(Principal principal, RemoveMemberRequest removeMemberRequest) {
        UserEntity agent = userRepository.findById(Long.parseLong(principal.getName())).orElseThrow(null);
        verifyAgent(agent);

        UserEntity user = userRepository.findById(removeMemberRequest.getUserid()).orElseThrow(null);
        verifyUser(user);

        if (user.getGroup() == null || !user.getGroup().equals(agent.getGroup()))
            throw new CustomException(GroupErrorCode.USER_NOT_INGROUP);

        user.setGroup(null);
        user.setRole(null);
        userRepository.save(user);

        return ResponseEntity.ok(new SuccessResponse("removeMember successful"));
    }

    public ResponseEntity<SuccessResponse> deleteGroup(Principal principal) {
        UserEntity agent = userRepository.findById(Long.parseLong(principal.getName())).orElseThrow(null);
        verifyAgent(agent);

        GroupEntity group = agent.getGroup();
        verifyGroup(group);

        group.getUsers().forEach(user -> user.setGroup(null));
        group.getUsers().forEach(user -> user.setRole(null));
        userRepository.saveAll(group.getUsers());

        groupRepository.delete(group);

        return ResponseEntity.ok(new SuccessResponse("deleteGroup successful"));
    }

    public ResponseEntity<SuccessResponse> leaveGroup(Principal principal) {
        UserEntity user = userRepository.findById(Long.parseLong(principal.getName())).orElseThrow(null);
        verifyUser(user);

        user.setGroup(null);
        user.setRole(null);
        userRepository.save(user);

        return ResponseEntity.ok(new SuccessResponse("leaveGroup successful"));
    }


}

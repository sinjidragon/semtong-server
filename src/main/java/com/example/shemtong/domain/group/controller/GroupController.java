package com.example.shemtong.domain.group.controller;

import com.example.shemtong.domain.group.dto.request.JoinGroupRequest;
import com.example.shemtong.domain.group.dto.request.RemoveMemberRequest;
import com.example.shemtong.domain.group.dto.response.CreateGroupResponse;
import com.example.shemtong.domain.group.dto.response.GetGroupResponse;
import com.example.shemtong.domain.group.dto.response.JoinGroupResponse;
import com.example.shemtong.domain.group.service.GroupService;
import com.example.shemtong.global.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Group", description = "그룹 관리 API")
@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Operation(summary = "그룹 생성")
    @PostMapping
    public ResponseEntity<CreateGroupResponse> createGroup(Principal principal) {
        return groupService.createGroup(principal);
    }

    @Operation(summary = "그룹 참여")
    @PostMapping("/join")
    public ResponseEntity<JoinGroupResponse> joinGroup(Principal principal, @RequestBody JoinGroupRequest joinGroupRequest) {
        return groupService.joinGroup(principal, joinGroupRequest);
    }

    @Operation(summary = "그룹 조회")
    @GetMapping("/members")
    public ResponseEntity<GetGroupResponse> getGroup(Principal principal) {
        return groupService.getGroup(principal);
    }

    @Operation(summary = "그룹에서 멤버 추방")
    @DeleteMapping("/member")
    public ResponseEntity<SuccessResponse> removeMember(Principal principal, @RequestBody RemoveMemberRequest removeMemberRequest) {
        return groupService.removeMember(principal, removeMemberRequest);
    }

    @Operation(summary = "그룹 삭제")
    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteGroup(Principal principal) {
        return groupService.deleteGroup(principal);
    }

    @Operation(summary = "그룹 탈퇴")
    @PostMapping("/leave")
    public ResponseEntity<SuccessResponse> leaveGroup(Principal principal) {
        return groupService.leaveGroup(principal);
    }

}

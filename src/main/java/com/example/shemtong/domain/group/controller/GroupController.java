package com.example.shemtong.domain.group.controller;

import com.example.shemtong.domain.group.dto.request.JoinGroupRequest;
import com.example.shemtong.domain.group.dto.request.RemoveMemberRequest;
import com.example.shemtong.domain.group.service.GroupService;
import com.example.shemtong.domain.user.Entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Operation(summary = "그룹 생성")
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(Principal principal) {
        return groupService.createGroup(principal);
    }

    @Operation(summary = "그룹 참여")
    @PostMapping("/join")
    public ResponseEntity<?> joinGroup(Principal principal, @RequestBody JoinGroupRequest joinGroupRequest) {
        return groupService.joinGroup(principal, joinGroupRequest);
    }

    @Operation(summary = "그룹 조회")
    @GetMapping("/members")
    public ResponseEntity<?> getGroup(Principal principal) {
        return groupService.getGroup(principal);
    }

    @Operation(summary = "그룹에서 멤버 추방")
    @DeleteMapping("/member")
    public ResponseEntity<?> removeMember(Principal principal, @RequestBody RemoveMemberRequest removeMemberRequest) {
        return groupService.removeMember(principal, removeMemberRequest);
    }

    @Operation(summary = "그룹 삭제")
    @DeleteMapping
    public ResponseEntity<?> deleteGroup(Principal principal) {
        return groupService.deleteGroup(principal);
    }

    @Operation(summary = "그룹 탈퇴")
    @PostMapping("/leave")
    public ResponseEntity<?> leaveGroup(Principal principal) {
        return groupService.leaveGroup(principal);
    }

}

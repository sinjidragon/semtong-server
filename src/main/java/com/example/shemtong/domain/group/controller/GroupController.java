package com.example.shemtong.domain.group.controller;

import com.example.shemtong.domain.group.dto.request.JoinGroupRequest;
import com.example.shemtong.domain.group.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

}

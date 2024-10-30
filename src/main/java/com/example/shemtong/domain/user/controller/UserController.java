package com.example.shemtong.domain.user.controller;

import com.example.shemtong.domain.user.dto.UserResponse;
import com.example.shemtong.domain.user.service.UserService;
import com.example.shemtong.global.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "User", description = "user API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 정보 얻기")
    @GetMapping
    public ResponseEntity<UserResponse> getUser(Principal principal) {
        return userService.getUser(principal);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteUser(Principal principal) {
        return userService.deleteUser(principal);
    }
}

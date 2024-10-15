package com.example.shemtong.controller;

import com.example.shemtong.dto.login.LoginRequest;
import com.example.shemtong.dto.signup.SignupRequest;
import com.example.shemtong.jwt.JwtUtil;
import com.example.shemtong.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;
    private JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> joinUser(@RequestBody SignupRequest signupRequest) {
        return authService.joinUser(signupRequest);
    }

/*
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        return authService.loginUser(loginRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshUser(@RequestHeader("Authorization") String refreshToken) {
        String token = refreshToken.substring(7);

        if(jwtUtil.isTokenValid(token)) {
            return authService.refreshUser(token);
        }else{
            return ResponseEntity.status(401).body(new ErrorResponse("토큰을 찾을수 없습니다.", "토큰 유효성 오류"));
        }
    }
*/

}

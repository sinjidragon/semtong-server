package com.example.shemtong.domain.auth.controller;

import com.example.shemtong.domain.auth.dto.EmailRequest;
import com.example.shemtong.global.dto.ErrorResponse;
import com.example.shemtong.domain.auth.dto.login.LoginRequest;
import com.example.shemtong.domain.auth.dto.signup.SignupRequest;
import com.example.shemtong.domain.auth.jwt.JwtUtil;
import com.example.shemtong.domain.auth.service.AuthService;
import com.example.shemtong.domain.auth.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;
    private MailService mailService;
    private JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil, MailService mailService) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.mailService = mailService;
    }

    @Operation(summary = "아이디 중복 확인")
    @GetMapping("/checkusername")
    public ResponseEntity<?> checkUsername(@RequestParam("username") String username) {
        return authService.checkUsername(username);
    }

    @Operation(summary = "이메일 중복 확인, 인증코드 발송")
    @PostMapping("/sendmail")
    public ResponseEntity<?> sendMail(@RequestParam("email") String email) throws MessagingException {
        return mailService.sendMail(email);
    }

    @Operation(summary = "인증코드 확인")
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody EmailRequest emailRequest) {
        return mailService.verify(emailRequest);
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<?> joinUser(@RequestBody SignupRequest signupRequest) {
        return authService.joinUser(signupRequest);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        return authService.loginUser(loginRequest);
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshUser(@RequestHeader("Authorization") String refreshToken) {
        String token = refreshToken.substring(7);

        if(jwtUtil.isTokenValid(token)) {
            return authService.refreshUser(token);
        }else{
            return ResponseEntity.status(401).body(new ErrorResponse("토큰을 찾을수 없습니다.", "토큰 유효성 오류"));
        }
    }


}

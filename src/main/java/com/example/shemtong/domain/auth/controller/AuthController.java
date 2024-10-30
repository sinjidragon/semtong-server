package com.example.shemtong.domain.auth.controller;

import com.example.shemtong.domain.auth.dto.EmailRequest;
import com.example.shemtong.domain.auth.dto.login.LoginResponse;
import com.example.shemtong.domain.auth.dto.refresh.RefreshRequest;
import com.example.shemtong.domain.auth.dto.refresh.RefreshResponse;
import com.example.shemtong.domain.auth.exception.AuthErrorCode;
import com.example.shemtong.global.dto.ErrorResponse;
import com.example.shemtong.domain.auth.dto.login.LoginRequest;
import com.example.shemtong.domain.auth.dto.signup.SignupRequest;
import com.example.shemtong.domain.auth.jwt.JwtUtil;
import com.example.shemtong.domain.auth.service.AuthService;
import com.example.shemtong.domain.auth.service.MailService;
import com.example.shemtong.global.dto.SuccessResponse;
import com.example.shemtong.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "auth API")
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "아이디 중복 확인")
    @GetMapping("/checkusername")
    public ResponseEntity<SuccessResponse> checkUsername(@RequestParam("username") String username) {
        return authService.checkUsername(username);
    }

    @Operation(summary = "이메일 중복 확인, 인증코드 발송")
    @PostMapping("/sendmail")
    public ResponseEntity<SuccessResponse> sendMail(@RequestParam("email") String email) throws MessagingException {
        return mailService.sendMail(email);
    }

    @Operation(summary = "인증코드 확인")
    @PostMapping("/verify")
    public ResponseEntity<SuccessResponse> verify(@RequestBody EmailRequest emailRequest) {
        return mailService.verify(emailRequest);
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> joinUser(@RequestBody SignupRequest signupRequest) {
        return authService.joinUser(signupRequest);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        return authService.loginUser(loginRequest);
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshUser(@RequestBody RefreshRequest refreshRequest) {
        log.info("refresh token 1");
        if(jwtUtil.isTokenValid(refreshRequest.getRefreshToken())) {
            return authService.refreshUser(refreshRequest.getRefreshToken());
        }else{
            throw new CustomException(AuthErrorCode.TOKEN_NOT_FOUND);
        }
    }


}

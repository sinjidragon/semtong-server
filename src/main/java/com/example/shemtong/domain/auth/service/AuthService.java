package com.example.shemtong.domain.auth.service;

import com.example.shemtong.domain.auth.dto.RefreshResponse;
import com.example.shemtong.domain.user.Entity.UserEntity;
import com.example.shemtong.domain.auth.jwt.JwtUtil;
import com.example.shemtong.global.dto.ErrorResponse;
import com.example.shemtong.domain.auth.dto.login.LoginRequest;
import com.example.shemtong.domain.auth.dto.login.LoginResponse;
import com.example.shemtong.domain.auth.dto.signup.SignupRequest;
import com.example.shemtong.global.dto.SuccessResponse;
import com.example.shemtong.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> checkUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("checkUsername failed","아이디가 이미 사용중입니다"));
        }
        return ResponseEntity.ok(new SuccessResponse("checkUsername successful"));
    }

    public ResponseEntity<?> joinUser(SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.username()).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("signup failed","아이디가 이미 사용중입니다"));
        }

        if (userRepository.findByEmail(signupRequest.email()).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("signup failed","이미 사용중인 이메일입니다"));
        }

        UserEntity userEntity = UserEntity.builder()
                .username(signupRequest.username())
                .password((passwordEncoder.encode(signupRequest.password())))
                .email(signupRequest.email())
                .build();

        userRepository.save(userEntity);

        return ResponseEntity.ok(new SuccessResponse("signup successful"));

    }


    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.username())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("login failed", "유저를 찾을 수 없습니다."));
        }

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("login failed","비밀번호가 일치하지 않습니다."));
        }

        String token = jwtUtil.generateToken(user.getUid().toString());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUid().toString());

        String userRole = user.getRole() != null ? user.getRole().name() : null;

        return ResponseEntity.ok(new LoginResponse(userRole, token, refreshToken, "bearer"));
    }

    public ResponseEntity<?> refreshUser(String token) {
        Long id = jwtUtil.extractUserId(token);
        if (jwtUtil.isTokenValid(token)) {
            String accessToken = jwtUtil.generateToken(id.toString());
            String refreshToken = jwtUtil.generateRefreshToken(id.toString());

            return ResponseEntity.ok(new RefreshResponse(accessToken, refreshToken, "bearer"));
        }else{
            return ResponseEntity.badRequest().body(new ErrorResponse("Token Refresh Failed","Token is not found."));
        }
    }

}

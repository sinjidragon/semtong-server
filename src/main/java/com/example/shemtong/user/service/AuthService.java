package com.example.shemtong.user.service;

import com.example.shemtong.user.Entity.UserEntity;
import com.example.shemtong.user.dto.ErrorResponse;
import com.example.shemtong.user.dto.login.LoginRequest;
import com.example.shemtong.user.dto.login.LoginResponse;
import com.example.shemtong.user.dto.signup.SignupRequest;
import com.example.shemtong.user.dto.SuccessResponse;
import com.example.shemtong.user.jwt.JwtUtil;
import com.example.shemtong.user.repository.UserRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
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

        UserEntity userEntity = UserEntity.builder()
                .username(signupRequest.username())
                .password((passwordEncoder.encode(signupRequest.password())))
                .email(signupRequest.email())
                .build();

        userRepository.save(userEntity);

        return ResponseEntity.ok(new SuccessResponse("join successful"));

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

        return ResponseEntity.ok(new LoginResponse(token, refreshToken, "bearer"));
    }

    public ResponseEntity<?> refreshUser(String token) {
        Long id = jwtUtil.extractUserId(token);
        if (jwtUtil.isTokenValid(token)) {
            String accessToken = jwtUtil.generateToken(id.toString());
            String refreshToken = jwtUtil.generateRefreshToken(id.toString());

            return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, "bearer"));
        }else{
            return ResponseEntity.badRequest().body(new ErrorResponse("Token Refresh Failed","Token is not found."));
        }
    }

}

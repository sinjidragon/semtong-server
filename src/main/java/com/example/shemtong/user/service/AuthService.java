package com.example.shemtong.user.service;

import com.example.shemtong.user.Entity.UserEntity;
import com.example.shemtong.user.dto.ErrorResponse;
import com.example.shemtong.user.dto.signup.SignupRequest;
import com.example.shemtong.user.dto.SuccessResponse;
import com.example.shemtong.user.jwt.JwtUtil;
import com.example.shemtong.user.repository.UserRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
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


}

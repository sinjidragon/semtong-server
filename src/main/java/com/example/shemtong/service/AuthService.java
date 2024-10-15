package com.example.shemtong.service;

import com.example.shemtong.Entity.UserEntity;
import com.example.shemtong.dto.ErrorResponse;
import com.example.shemtong.dto.signup.SignupRequest;
import com.example.shemtong.dto.signup.SignupResponse;
import com.example.shemtong.jwt.JwtUtil;
import com.example.shemtong.repository.UserRepository;
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

    public ResponseEntity<?> joinUser(SignupRequest signupRequest) {

        if (userRepository.findByUsername(signupRequest.username()).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("join failed","이미 사용중인 아이디입니다."));
        }

        if (userRepository.findByEmail(signupRequest.email()).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("join failed","이미 사용중인 이메일입니다."));
        }

        UserEntity userEntity = UserEntity.builder()
                .username(signupRequest.username())
                .password((passwordEncoder.encode(signupRequest.password())))
                .email(signupRequest.email())
                .build();

        userRepository.save(userEntity);

        return ResponseEntity.ok(new SignupResponse("join successful"));
    }
}

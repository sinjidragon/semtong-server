package com.example.shemtong.domain.auth.service;

import com.example.shemtong.domain.auth.dto.refresh.RefreshResponse;
import com.example.shemtong.domain.auth.exception.AuthErrorCode;
import com.example.shemtong.domain.user.Entity.UserEntity;
import com.example.shemtong.domain.auth.jwt.JwtUtil;
import com.example.shemtong.domain.user.Entity.UserState;
import com.example.shemtong.domain.user.exception.UserErrorCode;
import com.example.shemtong.domain.auth.dto.login.LoginRequest;
import com.example.shemtong.domain.auth.dto.login.LoginResponse;
import com.example.shemtong.domain.auth.dto.signup.SignupRequest;
import com.example.shemtong.global.dto.SuccessResponse;
import com.example.shemtong.domain.user.repository.UserRepository;
import com.example.shemtong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.example.shemtong.domain.user.Entity.UserState.CREATED;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;

    public void verifyUser(UserEntity user) {
        if (user == null)
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);

        if (user.getState() == UserState.DELETED)
            throw new CustomException(UserErrorCode.USER_IS_DELETED);
    }

    public void verifyUsername(String username) {
        if (userRepository.findByUsername(username).isPresent())
            throw new CustomException(AuthErrorCode.USERNAME_ALREADY_EXIST);
    }

    public ResponseEntity<SuccessResponse> checkUsername(String username) {
        verifyUsername(username);
        return ResponseEntity.ok(new SuccessResponse("checkUsername successful"));
    }

    public ResponseEntity<SuccessResponse> joinUser(SignupRequest signupRequest) {
        verifyUsername(signupRequest.username());

        if (userRepository.findByEmail(signupRequest.email()).isPresent())
            throw new CustomException(AuthErrorCode.EMAIL_ALREADY_EXIST);

        UserEntity userEntity = UserEntity.builder()
                .username(signupRequest.username())
                .password((passwordEncoder.encode(signupRequest.password())))
                .email(signupRequest.email())
                .state(CREATED)
                .build();

        userRepository.save(userEntity);

        return ResponseEntity.ok(new SuccessResponse("signup successful"));

    }


    public ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.username()).orElse(null);
        verifyUser(user);

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword()))
            throw new CustomException(AuthErrorCode.PASSWORD_NOT_MATCH);

        String token = jwtUtil.generateToken(user.getUid().toString());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUid().toString());

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        if (ops.get(user.getUid().toString()) != null)
            redisTemplate.delete(user.getUid().toString());

        ops.set(user.getUid().toString(), refreshToken, 10080, TimeUnit.MINUTES);

        String userRole = user.getRole() != null ? user.getRole().name() : null;

        return ResponseEntity.ok(new LoginResponse(userRole, token, refreshToken, "bearer"));
    }

    public ResponseEntity<RefreshResponse> refreshUser(String token) {
        log.info("refresh token 2");
        Long id = jwtUtil.extractUserId(token);

        UserEntity user = userRepository.findById(id).orElse(null);
        verifyUser(user);

        String userRole = user.getRole() != null ? user.getRole().name() : null;

        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        if (jwtUtil.isTokenValid(token) && ops.get(id.toString()) != token) {
            String accessToken = jwtUtil.generateToken(id.toString());
            String refreshToken = jwtUtil.generateRefreshToken(id.toString());

            redisTemplate.delete(id.toString());
            ops.set(id.toString(), refreshToken, 10080, TimeUnit.MINUTES);

            return ResponseEntity.ok(new RefreshResponse(userRole,accessToken, refreshToken, "bearer"));
        }else{
            throw new CustomException(AuthErrorCode.TOKEN_NOT_FOUND);
        }


    }

}

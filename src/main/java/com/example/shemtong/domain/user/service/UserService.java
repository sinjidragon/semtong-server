package com.example.shemtong.domain.user.service;

import com.example.shemtong.domain.user.Entity.UserEntity;
import com.example.shemtong.domain.user.Entity.UserState;
import com.example.shemtong.domain.user.dto.UserResponse;
import com.example.shemtong.domain.user.exception.UserErrorCode;
import com.example.shemtong.domain.user.repository.UserRepository;
import com.example.shemtong.global.dto.ErrorResponse;
import com.example.shemtong.global.dto.SuccessResponse;
import com.example.shemtong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void verifyUser(UserEntity user) {
        if (user == null)
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);

        if (user.getState() == UserState.DELETED)
            throw new CustomException(UserErrorCode.USER_IS_DELETED);
    }

    public ResponseEntity<UserResponse> getUser(Principal principal) {
        UserEntity user = userRepository.findByUid(Long.valueOf(principal.getName())).orElseThrow(null);
        verifyUser(user);

        return ResponseEntity.ok(UserResponse.fromUserEntity(user));
    }

    public ResponseEntity<SuccessResponse> deleteUser(Principal principal) {
        UserEntity user = userRepository.findByUid(Long.valueOf(principal.getName())).orElseThrow(null);
        verifyUser(user);

        user.setState(UserState.DELETED);
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok(new SuccessResponse("User deleted successfully"));
    }
}

package com.example.shemtong.domain.user.service;

import com.example.shemtong.domain.group.service.GroupService;
import com.example.shemtong.domain.user.entity.UserEntity;
import com.example.shemtong.domain.user.entity.UserRole;
import com.example.shemtong.domain.user.entity.UserState;
import com.example.shemtong.domain.user.dto.UserResponse;
import com.example.shemtong.domain.user.exception.UserErrorCode;
import com.example.shemtong.domain.user.repository.UserRepository;
import com.example.shemtong.global.dto.SuccessResponse;
import com.example.shemtong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GroupService groupService;

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

        if (user.getRole() == UserRole.AGENT)
            groupService.deleteGroup(principal);

        user.setRole(null);
        user.setGroup(null);
        user.setState(UserState.DELETED);
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok(new SuccessResponse("User deleted successfully"));
    }
}

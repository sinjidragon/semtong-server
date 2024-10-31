package com.example.shemtong.domain.user.repository;

import com.example.shemtong.domain.user.Entity.UserEntity;
import com.example.shemtong.domain.user.Entity.UserState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUid(Long userId);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByStateAndDeletedAtBefore(UserState state, LocalDateTime date);
}

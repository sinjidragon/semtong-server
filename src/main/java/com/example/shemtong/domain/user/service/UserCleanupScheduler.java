package com.example.shemtong.domain.user.service;

import com.example.shemtong.domain.user.entity.UserEntity;
import com.example.shemtong.domain.user.entity.UserState;
import com.example.shemtong.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCleanupScheduler {

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredUsers() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minus(3, ChronoUnit.MONTHS);

        log.info("User cleanup scheduler triggered at: {}", LocalDateTime.now());

        List<UserEntity> usersToDelete = userRepository.findByStateAndDeletedAtBefore(UserState.DELETED, threeMonthsAgo);

        log.info("Found {} users to delete", usersToDelete.size());

        userRepository.deleteAll(usersToDelete);
    }

}

package com.example.shemtong.domain.group.repository;

import com.example.shemtong.domain.group.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    Optional<GroupEntity> findByGroupcode(String groupcode);
}

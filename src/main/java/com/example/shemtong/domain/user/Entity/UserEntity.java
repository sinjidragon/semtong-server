package com.example.shemtong.domain.user.Entity;

import com.example.shemtong.domain.group.entity.GroupEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@SuperBuilder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid; // 유저 식별할때 사용

    @Column(nullable = false, unique = true)
    private String username; // 로그인할때 사용

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private GroupEntity group;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserState state = UserState.CREATED;

    private LocalDateTime deletedAt;

}

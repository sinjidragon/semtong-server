package com.example.shemtong.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

/*
    @Column(nullable = false)
    private String user; // 유저 성명 이게 필요하나???

    private String role; // 권한
*/

}

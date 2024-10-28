package com.example.shemtong.domain.group.entity;

import com.example.shemtong.domain.user.Entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "groups_info")
@SuperBuilder
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupname;

    @Column(nullable = false, unique = true)
    private String groupcode;

    @OneToMany(mappedBy = "group")
    @JsonManagedReference
    private List<UserEntity> users;

}

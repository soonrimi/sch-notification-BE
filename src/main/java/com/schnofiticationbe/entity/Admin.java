package com.schnofiticationbe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "admin")
@Schema(requiredProperties = {"id", "userId", "passwordHash", "name", "role", "affiliation"})

public class Admin {
    public enum Role {
        SUPER_ADMIN,
        ADMIN
    }

    // getter/setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // PK

    @Setter
    @Column(nullable = false, unique = true, length = 50)
    private String userId;   // 아이디

    @Setter
    @Column(nullable = false)
    private String passwordHash;   // 비밀번호 해시

    @Setter
    @Column(nullable = false, length = 100)
    private String name;   // 이름

    @Setter
    @Column(nullable = false)
    private Role role;   // 권한

    @Setter
    @Column(length = 50)
    private String affiliation; // 소속

}


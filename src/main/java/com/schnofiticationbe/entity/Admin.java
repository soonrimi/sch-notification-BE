package com.schnofiticationbe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Entity
@Table(name = "admin")
@Schema(requiredProperties = {"id", "userId", "passwordHash", "name", "role", "affiliation"})

public class Admin {
    public enum Affiliation {
        DEPARTMENT,
        GRADE,
        STUDENT_COUNCIL,
        FACULTY_STAFF,
    }

    // getter/setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // PK

    @Setter
    @Column(nullable = false, unique = true, length = 50)
    private String userId;   // 이메일

    @Getter
    @Setter
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false; // 이메일 인증 여부

    @Getter
    @Setter
    private String emailVerificationToken;

    @Setter
    @Column(nullable = false)
    private String passwordHash;   // 비밀번호 해시

    @Setter
    @Column(nullable = false, length = 100)
    private String name;   // 이름

    @Setter
    @Column(length = 50)
    private Affiliation affiliation; // 소속

    @Setter
    @ElementCollection(targetClass = Category.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "admin_categories", joinColumns = @JoinColumn(name = "admin_id"))
    @Column(name = "category")
    private Set<Category> categories;

    @Setter
    @ManyToMany()
    @JoinTable(
            name = "admin_department",
            joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private Set<Department> departments; // 학과

    @Setter
    @ElementCollection(targetClass = TargetYear.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "admin_grades", joinColumns = @JoinColumn(name = "admin_id"))
    @Column(name = "grade")
    private Set<TargetYear> grades;

}

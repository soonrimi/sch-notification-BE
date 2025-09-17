package com.schnofiticationbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Entity
@Table(name = "admin")
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
    private String userId;   // 아이디

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

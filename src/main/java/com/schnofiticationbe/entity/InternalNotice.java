package com.schnofiticationbe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Schema(requiredProperties = {"id", "title", "content", "createdAt", "viewCount", "writer", "targetYear", "targetDept", "sentToKakao"})
@DiscriminatorValue("InternalNotice")
public class InternalNotice extends Notice {


    @ManyToOne
    @JoinColumn(name = "writer", nullable = false)
    private Admin writer; // 실제 작성자

    @Column(nullable = false)
    private TargetYear targetYear;

    @ManyToMany()
    @JoinTable(
            name = "internal_notice_target_dept",
            joinColumns = @JoinColumn(name = "internal_notice_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private Set<Department> targetDept;

    @Column(nullable = false)
    private boolean sentToKakao;
}


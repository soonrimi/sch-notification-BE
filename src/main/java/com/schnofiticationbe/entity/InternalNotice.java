package com.schnofiticationbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("INTERNAL")
public class InternalNotice extends Notice {
    @ManyToOne
    @JoinColumn(name = "writer", nullable = false)
    private Admin writer; // 실제 작성자

    private String targetYear;  // "1,2,3,전체"
    private String targetDept;  // "컴퓨터공학과, 전체"

    // InternalNotice만의 추가 필드가 있다면 여기에 작성
}

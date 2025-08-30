package com.schnofiticationbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("InternalNotice")
public class InternalNotice extends Notice {
    public enum TargetYear {
        FIRST_YEAR("1학년"),
        SECOND_YEAR("2학년"),
        THIRD_YEAR("3학년"),
        ALL_YEARS("전체");

        private final String description;

        TargetYear(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @ManyToOne
    @JoinColumn(name = "writer", nullable = false)
    private Admin writer; // 실제 작성자

    @Column(nullable = false)
    private TargetYear targetYear;  // "1,2,3,전체"

    @Column(nullable = false)
    private String targetDept;  // "컴퓨터공학과, 전체"

    // InternalNotice만의 추가 필드가 있다면 여기에 작성
}

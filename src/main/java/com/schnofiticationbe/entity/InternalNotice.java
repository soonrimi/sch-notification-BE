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
        ALL_YEARS("전체"),
        FIRST_YEAR("1학년"),
        SECOND_YEAR("2학년"),
        THIRD_YEAR("3학년"),
        FOURTH_YEAR("4학년");

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
    private TargetYear targetYear;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Department targetDept;

    @Column(nullable = false)
    private boolean sentToKakao;
}

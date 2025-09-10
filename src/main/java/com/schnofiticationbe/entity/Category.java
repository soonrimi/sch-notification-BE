package com.schnofiticationbe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)

    @Schema(description = "카테고리 이름", example = "공지사항")
    private String categoryName;

<<<<<<< HEAD
    @OneToMany(mappedBy = "category")
    private Set<FetchedNotice> fetchedNotices = new LinkedHashSet<>();

=======
>>>>>>> 0a5c4b4 (feat : 카테고리 종류 조회 api 추가)
    @Builder
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

<<<<<<< HEAD


=======
>>>>>>> 0a5c4b4 (feat : 카테고리 종류 조회 api 추가)
}

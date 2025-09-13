package com.schnofiticationbe.dto;


import com.schnofiticationbe.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor

public class CategoryDto {
    private Long id;
    private String categoryName;

    public static class CreateRequest {
        private String categoryName;
    }
    public static class UpdateRequest {
        private String categoryName;
    }

    @Getter
    public static class CategoryResponse {
        private Long id;
        private String categoryName;

        public CategoryResponse(Category category) {
            this.id = category.getId();
            this.categoryName = category.getCategoryName();
        }
    }

}

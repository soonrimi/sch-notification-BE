package com.schnofiticationbe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor

class CategoryDto {
    private Long id;
    private String categoryName;

    public static class CreateRequest {
        private String categoryName;
    }
    public static class UpdateRequest {
        private String categoryName;
    }
    public static class CategoryResponse {
        private Long id;
        private String categoryName;

        public CategoryResponse(Long id, String categoryName) {
            this.id = id;
            this.categoryName = categoryName;
        }
    }

}

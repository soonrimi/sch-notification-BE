package com.schnofiticationbe.dto;

<<<<<<< HEAD
=======
import com.schnofiticationbe.entity.Category;
>>>>>>> 0a5c4b4 (feat : 카테고리 종류 조회 api 추가)
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor

<<<<<<< HEAD
class CategoryDto {
=======
public class CategoryDto {
>>>>>>> 0a5c4b4 (feat : 카테고리 종류 조회 api 추가)
    private Long id;
    private String categoryName;

    public static class CreateRequest {
        private String categoryName;
    }
    public static class UpdateRequest {
        private String categoryName;
    }
<<<<<<< HEAD
=======
    @Getter
>>>>>>> 0a5c4b4 (feat : 카테고리 종류 조회 api 추가)
    public static class CategoryResponse {
        private Long id;
        private String categoryName;

<<<<<<< HEAD
        public CategoryResponse(Long id, String categoryName) {
            this.id = id;
            this.categoryName = categoryName;
=======
        public CategoryResponse(Category category) {
            this.id = category.getId();
            this.categoryName = category.getCategoryName();
>>>>>>> 0a5c4b4 (feat : 카테고리 종류 조회 api 추가)
        }
    }

}

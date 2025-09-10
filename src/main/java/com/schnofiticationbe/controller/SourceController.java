package com.schnofiticationbe.controller;

<<<<<<< HEAD
=======
import com.schnofiticationbe.dto.CategoryDto;
>>>>>>> 0a5c4b4 (feat : 카테고리 종류 조회 api 추가)
import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.service.CategoryService;
import com.schnofiticationbe.service.CrawlPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
public class SourceController {
    private final CrawlPostService crawlPostService;
    private final CategoryService categoryService;
    //전체 카테고리 종류 조회
    @GetMapping("/tag")
<<<<<<< HEAD
    public ResponseEntity<List<String>> getTags() {
        List<Category> categories = categoryService.getAllCategories();
        List<String> tags = categories.stream()
                .map(category -> category.getCategoryName().toLowerCase(Locale.ROOT))
                .toList();
        return ResponseEntity.ok(tags);
=======
    public ResponseEntity<List<CategoryDto.CategoryResponse>> getAllTags(){
        return ResponseEntity.ok(categoryService.getAllCategories());
>>>>>>> 0a5c4b4 (feat : 카테고리 종류 조회 api 추가)
    }
}

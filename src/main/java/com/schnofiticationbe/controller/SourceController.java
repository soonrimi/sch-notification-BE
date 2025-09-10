package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.CategoryDto;
import com.schnofiticationbe.service.CategoryService;
import com.schnofiticationbe.service.CrawlPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class SourceController {
    private final CrawlPostService crawlPostService;
    private final CategoryService categoryService;
    //전체 카테고리 종류 조회
    @GetMapping("/tag")
    public ResponseEntity<List<CategoryDto.CategoryResponse>> getAllTags() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}

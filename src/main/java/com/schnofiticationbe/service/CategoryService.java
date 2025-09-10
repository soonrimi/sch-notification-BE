package com.schnofiticationbe.service;

<<<<<<< HEAD
=======
import com.schnofiticationbe.dto.CategoryDto;
import com.schnofiticationbe.dto.CategoryDto.CategoryResponse;
>>>>>>> 0a5c4b4 (feat : 카테고리 종류 조회 api 추가)
import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CategoryService {
    private final CategoryRepository categoryRepository;

<<<<<<< HEAD
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
=======
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDto.CategoryResponse::new)
                .toList();
>>>>>>> 0a5c4b4 (feat : 카테고리 종류 조회 api 추가)
    }
}

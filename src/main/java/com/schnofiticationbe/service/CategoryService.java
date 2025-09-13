package com.schnofiticationbe.service;


import com.schnofiticationbe.dto.CategoryDto;
import com.schnofiticationbe.dto.CategoryDto.CategoryResponse;
import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CategoryService {
    private final CategoryRepository categoryRepository;


    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDto.CategoryResponse::new)
                .toList();
    }
}

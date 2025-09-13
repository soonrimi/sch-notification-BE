package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}

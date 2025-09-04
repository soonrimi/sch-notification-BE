package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Department, Long> {
}

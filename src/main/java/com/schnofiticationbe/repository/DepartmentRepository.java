package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}


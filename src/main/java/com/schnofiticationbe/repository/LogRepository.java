package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.KeywordNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<KeywordNotification, Integer> {
}

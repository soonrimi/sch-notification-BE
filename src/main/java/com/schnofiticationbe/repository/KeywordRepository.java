package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.KeywordNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<KeywordNotification, Integer> {

    // device 기준 조회
    List<KeywordNotification> findByDevice(String device);
}

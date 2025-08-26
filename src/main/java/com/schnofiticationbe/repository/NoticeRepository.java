package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}

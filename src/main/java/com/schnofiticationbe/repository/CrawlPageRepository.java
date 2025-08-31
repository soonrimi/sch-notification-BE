package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.CrawlPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawlPageRepository extends JpaRepository <CrawlPage, Long> {
}

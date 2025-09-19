package com.schnofiticationbe.repository;

import com.schnofiticationbe.dto.NoticeDto;
import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Query("SELECT n FROM Notice n ORDER BY n.createdAt DESC")
    Page<Notice> findCombinedNoticesOrderByCreatedAtDesc(Pageable pageable);
    Page<Notice> findByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable);

    Page<Notice> findByCategory(Category category, Pageable pageable);

    Page<Notice> findByTitleContainingOrContentContaining(String keyword, String keyword1, Pageable pageable);
}

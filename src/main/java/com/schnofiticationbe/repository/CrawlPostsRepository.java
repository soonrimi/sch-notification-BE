package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.entity.CrawlPosts;
import com.schnofiticationbe.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PagedModel;

import java.util.List;
import java.util.Optional;

public interface CrawlPostsRepository extends JpaRepository<CrawlPosts, Long> {
    List<Notice> findByTitleContainingOrContentContaining(String title, String content); // 제목+내용 키워드로 찾기
    Page<CrawlPosts> findByCategory(Category categoryId, Pageable pageable);

    @Query("SELECT cp FROM CrawlPosts cp LEFT JOIN FETCH cp.CrawlAttachments WHERE cp.id = :id")
    Optional<CrawlPosts> findByIdWithAttachments(@Param("id") Long id);
}

package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.entity.CrawlPosts;
import com.schnofiticationbe.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrawlPostsRepository extends JpaRepository<CrawlPosts, Long> {
    List<Notice> findByTitleContainingOrContentContaining(String title, String content); // 제목+내용 키워드로 찾기
    List<CrawlPosts> findByCategoryId(Category categoryId);
}

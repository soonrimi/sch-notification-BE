package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.CrawlPostDto;
import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.entity.CrawlPosts;
import com.schnofiticationbe.entity.Notice;
import com.schnofiticationbe.repository.CrawlPageRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.schnofiticationbe.repository.CrawlPostsRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CrawlPostService {
    private final CrawlPostsRepository crawlPostsRepository;

    // 전체 공지 조회
    public Page<CrawlPostDto.CrawlPostsResponse> getAllCrawlNotices(Pageable pageable) {
        Page<CrawlPosts> posts= crawlPostsRepository.findAll(pageable);
        return posts.map(CrawlPostDto.CrawlPostsResponse::new);

    }

        crawlPost.setViewCount(crawlPost.getViewCount() + 1); // 조회수 증가
        CrawlPosts saved = crawlPostsRepository.save(crawlPost);
        return new CrawlPostDto.CrawlPostsResponse(saved);
    }


    public List<Notice> searchNotices(String keyword) {
        return crawlPostsRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }

    //카테고리별 공지사항 조회
    public Page<CrawlPostDto.CrawlPostsResponse> getNoticesByCategory(Category category, Pageable pageable) {
        // 1. Repository로부터 Page<CrawlPosts>를 받음
        Page<CrawlPosts> postsPage = crawlPostsRepository.findByCategory(category, pageable);
        // 2. Page의 map을 사용해 Page<CrawlPostDto.CrawlPostsResponse>로 변환하여 반환
        return postsPage.map(CrawlPostDto.CrawlPostsResponse::new);
    }

}

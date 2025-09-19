package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.CrawlPostDto;
import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.entity.CrawlPage;
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
    private final CrawlPageRepository crawlPageRepository;

    // 전체 공지 조회
    public Page<CrawlPostDto.CrawlPostsResponse> getAllCrawlNotices(Pageable pageable) {
        Page<CrawlPosts> posts= crawlPostsRepository.findAll(pageable);
        return posts.map(CrawlPostDto.CrawlPostsResponse::new);

    }



}

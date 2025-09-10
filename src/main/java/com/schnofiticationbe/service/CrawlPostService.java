package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.CrawlPostDto;
import com.schnofiticationbe.entity.CrawlPage;
import com.schnofiticationbe.entity.CrawlPosts;
import com.schnofiticationbe.entity.Notice;
import com.schnofiticationbe.repository.CrawlPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.schnofiticationbe.repository.CrawlPostsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlPostService {
    private final CrawlPostsRepository crawlPostsRepository;
    private final CrawlPageRepository crawlPageRepository;

    // 전체 공지 조회
    public List<CrawlPostDto.CrawlPostsResponse> getAllNotices() {
        return crawlPostsRepository.findAll()
                .stream()
                .map(CrawlPostDto.CrawlPostsResponse::new)
                .toList();
    }

    // 단일 공지 조회
    public CrawlPostDto.CrawlPostsResponse getNotice(Long id) {
        CrawlPosts crawlPost = crawlPostsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다."));

        crawlPost.setViewCount(crawlPost.getViewCount() + 1); // 조회수 증가
        CrawlPosts saved = crawlPostsRepository.save(crawlPost);
        return new CrawlPostDto.CrawlPostsResponse(saved);
    }


    public List<Notice> searchNotices(String keyword) {
        return crawlPostsRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }

    public List<String> getAllDepartments() {
        return crawlPageRepository.findAll()
                .stream()
                .map(CrawlPage::getTitle)
                .distinct()
                .toList();
    }
//카테고리별 공지사항 조회
    public List<CrawlPostDto.CrawlPostsResponse> getAllNoticesByCategoryId(Integer categoryId) {
        return crawlPostsRepository.findByCategoryId(categoryId)
                .stream()
                .map(CrawlPostDto.CrawlPostsResponse::new)
                .toList();
    }
}

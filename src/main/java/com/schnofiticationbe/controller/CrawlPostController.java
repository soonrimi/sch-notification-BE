package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.CrawlPostDto;

import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.entity.Notice;
import com.schnofiticationbe.service.CrawlPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class CrawlPostController {
    private final CrawlPostService noticeService;


    @GetMapping
    public ResponseEntity<Page<CrawlPostDto.CrawlPostsResponse>> getAllNotices(Pageable pageable) {
        return ResponseEntity.ok(noticeService.getAllNotices(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrawlPostDto.CrawlPostsResponse> getNotice(@PathVariable Long id) {

        return ResponseEntity.ok(noticeService.getNotice(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Notice>> searchNotices(@RequestParam String keyword) {
        return ResponseEntity.ok(noticeService.searchNotices(keyword));
    }

    //카테고리별 공지사항 조회 (및 전체 조회)
    @GetMapping("/category")
    public ResponseEntity<Page<CrawlPostDto.CrawlPostsResponse>> getNotices(
            @RequestParam(required = false) Category category,
            Pageable pageable){

        // 3. Page 객체를 미리 선언
        Page<CrawlPostDto.CrawlPostsResponse> postsPage;

        if (category != null) {
            postsPage = noticeService.getNoticesByCategory(category, pageable);
        } else {
            postsPage = noticeService.getAllNotices(pageable);
        }
        return ResponseEntity.ok(postsPage);

    }
}

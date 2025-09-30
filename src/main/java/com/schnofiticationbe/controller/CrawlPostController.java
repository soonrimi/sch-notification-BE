package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.NoticeDto;
import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class CrawlPostController {
    private final NoticeService noticeService;


    @GetMapping
    public ResponseEntity<Page<NoticeDto.ListResponse>> getAllNotices(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(noticeService.getCombinedNotices(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDto.DetailResponse> getNotice(@PathVariable Long id) {

        return ResponseEntity.ok(noticeService.getNotice(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<NoticeDto.ListResponse>> searchNotices(@RequestParam String keyword, @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(noticeService.searchNotices(keyword, pageable));
    }

    //카테고리별 공지사항 조회 (및 전체 조회)
    @GetMapping("/category")
    public ResponseEntity<Page<NoticeDto.ListResponse>> getNotices(
            @RequestParam(required = false) Category category,@ParameterObject Pageable pageable){
        Page<NoticeDto.ListResponse> postsPage;

        if (category != null) {
            postsPage = noticeService.getNoticesByCategory(category, pageable);
        } else {
            postsPage = noticeService.getAllNotices(pageable);
        }
        return ResponseEntity.ok(postsPage);

    }

    @GetMapping("/bookmark")
    public  ResponseEntity<Page<NoticeDto.ListResponse>> getNoticesByIds(
            @RequestBody(required = false) List<Long> ids,@ParameterObject Pageable pageable) {
        Page<NoticeDto.ListResponse> postsPage = noticeService.getNoticesByIds(ids, pageable);
        return ResponseEntity.ok(postsPage);
    }

}

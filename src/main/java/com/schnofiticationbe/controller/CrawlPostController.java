package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.CrawlPostDto;

import com.schnofiticationbe.entity.Notice;
import com.schnofiticationbe.service.CrawlPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class CrawlPostController {
    private final CrawlPostService noticeService;

    @GetMapping
    public ResponseEntity<List<CrawlPostDto.Response>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrawlPostDto.Response> getNotice(@PathVariable Long id) {

        return ResponseEntity.ok(noticeService.getNotice(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Notice>> searchNotices(@RequestParam String keyword) {
        return ResponseEntity.ok(noticeService.searchNotices(keyword));
    }
}

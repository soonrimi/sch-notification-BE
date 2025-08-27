package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.NoticeDto;

import com.schnofiticationbe.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<NoticeDto.Response>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDto.Response> getNotice(@PathVariable Long id) {

        return ResponseEntity.ok(noticeService.getNotice(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Notice>> searchNotices(@RequestParam String keyword) {
        return ResponseEntity.ok(noticeService.searchNotices(keyword));
    }
}

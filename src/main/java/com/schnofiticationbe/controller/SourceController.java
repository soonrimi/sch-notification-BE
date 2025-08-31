package com.schnofiticationbe.controller;

import com.schnofiticationbe.service.CrawlPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SourceController {
    private final CrawlPostService crawlPostService;
    //전체 학과 조회
    @GetMapping("/department" )
    public ResponseEntity<List<String>> getDepartments() {
        List<String> departments = crawlPostService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
}

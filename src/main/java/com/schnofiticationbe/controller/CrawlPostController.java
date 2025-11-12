package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.NoticeDto;
import com.schnofiticationbe.dto.SearchRequestDto;
import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.entity.Department;
import com.schnofiticationbe.entity.TargetYear;
import com.schnofiticationbe.service.NoticeService;
import lombok.Getter;
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
    //북마크 공지사항 조회 (및 북마크 내 검색)

    @PostMapping("/bookmark/search")
    public ResponseEntity<Page<NoticeDto.ListResponse>> searchNoticesByIds(
            @RequestBody SearchRequestDto requestDto, // RequestBody로 받음
            Pageable pageable) {
        List<Long> ids = requestDto.getIds();
        String keyword = requestDto.getKeyword();
        Page<NoticeDto.ListResponse> postsPage;
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.ok(Page.empty(pageable));
        }else if (keyword ==null || keyword.isEmpty()){
            postsPage = noticeService.getNoticesByIds(ids, pageable);
        }else {
            postsPage = noticeService.searchInBookmarkedNotices(ids, keyword, pageable);
        }
        return ResponseEntity.ok(postsPage);
    }

    //학과 및 학년별 공지사항 조회 (및 전체 조회)
    @GetMapping("/initialized")
    public ResponseEntity<Page<NoticeDto.ListResponse>> getInitializedNotices(
            @RequestParam (required = false, name = "departmentId")Long departmentId,
            @RequestParam (required = false, name = "targetYear")TargetYear targetYear,
            Pageable pageable) {
        Page<NoticeDto.ListResponse> postsPage;
        if (targetYear == null) {
            postsPage=noticeService.getAllNoticeByDepartment(departmentId, pageable);
        }else {
            postsPage=noticeService.getNoticesByDepartmentAndTargetYear(departmentId, targetYear, pageable);
        }
        return ResponseEntity.ok(postsPage);
    }

}

package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.DeptYearBundle;
import com.schnofiticationbe.dto.NoticeDto;
import com.schnofiticationbe.dto.SearchRequestDto;
import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.entity.Department;
import com.schnofiticationbe.entity.TargetYear;
import com.schnofiticationbe.service.NoticeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class CrawlPostController {
    private final NoticeService noticeService;


    @GetMapping
    public ResponseEntity<Page<NoticeDto.ListResponse>> getAllNotices(
            @RequestBody List<DeptYearBundle> deptYearBundles,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(noticeService.getCombinedNotices(deptYearBundles, pageable));
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

    //학과별 공지사항 조회(및 전체 학과 조회)
    @PostMapping("/initialized/department")
    public ResponseEntity<Page<NoticeDto.ListResponse>> getInitializedNoticesByDepartment(
            @RequestBody List<Long> departmentIds,
            Pageable pageable) {
        Page<NoticeDto.ListResponse> postsPage = noticeService.getAllNoticeByDepartment(departmentIds, pageable);
        return ResponseEntity.ok(postsPage);
    }

    //학과 및 학년별 공지사항 조회 (및 전체 조회)
    @PostMapping("/initialized/departmentyear")
    public ResponseEntity<Page<NoticeDto.ListResponse>> getInitializedNoticesByDepartmentAndYear(
            @RequestBody List<DeptYearBundle> deptYearBundles,
            Pageable pageable) {
        Page<NoticeDto.ListResponse> postsPage = noticeService.getNoticesByDepartmentAndTargetYear(deptYearBundles, pageable);
        return ResponseEntity.ok(postsPage);
    }

    @GetMapping("/categories/initialized")
    public ResponseEntity<List<Category>> getCategories(
            @RequestParam(required = false) List<Category> exclude
    ) {
        List<Category> categories = noticeService.getCategoriesExcept(exclude);
        return ResponseEntity.ok(categories);
    }

    // OG 썸네일 이미지 조회
    @GetMapping("/og-image/{id}")
    public ResponseEntity<Resource> getThumbnail(
            @PathVariable Long id,
            @RequestParam("sig") String sig) {
        System.out.println(">>> 컨트롤러 진입 성공! ID: " + id);

        try {
            Resource file = noticeService.getOgImageById(id, sig);

            if (file == null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        //todo: 기본 이미지 리소스 반환 (null-> 기본 이미지로 변경)
                        .body(null);
            }

            String filename = file.getFilename() != null ? file.getFilename().toLowerCase() : "";
            MediaType mediaType = MediaType.IMAGE_PNG; // 기본값
            if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else if (filename.endsWith(".gif")) {
                mediaType = MediaType.IMAGE_GIF;
            }

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                    .body(file);

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

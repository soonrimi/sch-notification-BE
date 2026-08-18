package com.schnofiticationbe.service;

import com.schnofiticationbe.Utils.PageUtils;
import com.schnofiticationbe.dto.DeptYearBundle;
import com.schnofiticationbe.dto.NoticeDto;
import com.schnofiticationbe.entity.*;
import com.schnofiticationbe.repository.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final DepartmentRepository departmentRepository;
    private final AttachmentRepository attachmentRepository;
    private final String UPLOAD_ROOT = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
    @Value("${app.base-url}")
    private String BASE_URL;

    @Value("${image-key}")
    private String SECRET_KEY;


    @Transactional
    public Page<NoticeDto.ListResponse> getCombinedNotices(List<DeptYearBundle> bundles, Pageable pageable) {

        List<Long> requestedDeptIds = bundles.stream()
                .map(DeptYearBundle::getDepartmentId)
                .toList();

        Map<Long, String> deptIdToNameMap = departmentRepository.findAllById(requestedDeptIds).stream()
                .collect(Collectors.toMap(Department::getId, Department::getName));
        List<String> targetDeptNames = new ArrayList<>(deptIdToNameMap.values());

        List<Object[]> matchedRows = new ArrayList<>();

        // 1단계: bundles에 매칭되는 InternalNotice ID & Date 목록 추출
        for (DeptYearBundle bundle : bundles) {
            List<Object[]> rows = noticeRepository.findInternalNoticeIdsAndDateByBundle(bundle.getDepartmentId(), bundle.getTargetYear().ordinal());
            if (rows != null) {
                matchedRows.addAll(rows);
            }
        }

        // 2단계: 학과 이름에 속하는 CrawlPosts ID & Date 목록 추출
        if (!targetDeptNames.isEmpty()) {
            List<Object[]> rows = noticeRepository.findCrawlPostIdsAndDateBySources(targetDeptNames);
            if (rows != null) {
                matchedRows.addAll(rows);
            }
        }

        List<String> generalCategories = List.of(
                Category.UNIVERSITY.name(), Category.RECRUIT.name(), Category.ACTIVITY.name(), Category.PROMOTION.name()
        );

        // 3단계: 일반 공지사항 ID & Date 목록 추출
        List<Object[]> rows = noticeRepository.findGeneralNoticeIdsAndDate(generalCategories);
        if (rows != null) {
            matchedRows.addAll(rows);
        }

        // 중복 제거 및 정렬
        Map<Long, Timestamp> uniqueNoticeMap = new HashMap<>();
        for (Object[] row : matchedRows) {
            Long id = (Long) row[0];
            Timestamp createdAt = (Timestamp) row[1];
            uniqueNoticeMap.put(id, createdAt);
        }

        List<Map.Entry<Long, Timestamp>> sortedEntries = new ArrayList<>(uniqueNoticeMap.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        int totalElements = sortedEntries.size();
        int startOffset = (int) pageable.getOffset();
        int endOffset = Math.min(startOffset + pageable.getPageSize(), totalElements);

        List<Long> pageIds = new ArrayList<>();
        if (startOffset < totalElements) {
            for (int i = startOffset; i < endOffset; i++) {
                pageIds.add(sortedEntries.get(i).getKey());
            }
        }

        if (pageIds.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Notice> notices = noticeRepository.findNoticesByMatchedIdsOnly(pageIds, PageUtils.toLatestOrder(pageable)).getContent();
        return new PageImpl<>(notices, pageable, totalElements).map(NoticeDto.ListResponse::new);
    }

    @Transactional
    public NoticeDto.DetailResponse getNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다."));

        // 조회수 증가
        notice.setViewCount(notice.getViewCount() + 1);
        noticeRepository.save(notice);

        // 보안 서명 생성 및 URL 조립
        String sig = generateSignature(String.valueOf(id));
        String ogImageUrl = BASE_URL + "/api/notice/og-image/" + id + "?sig=" + sig;
        return new NoticeDto.DetailResponse(notice, ogImageUrl);
    }



    public Page<NoticeDto.ListResponse> searchNotices(String keyword, Pageable pageable) {
        Page<Long> idPage = noticeRepository.findIdsByTitleOrContent(keyword, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        List<Long> ids = idPage.getContent();
        if (ids.isEmpty()) {
            return Page.empty(pageable);
        }
        List<Notice> notices = noticeRepository.findNoticesByMatchedIdsOnly(ids, PageUtils.toLatestOrder(pageable)).getContent();
        return new PageImpl<>(notices, pageable, idPage.getTotalElements()).map(NoticeDto.ListResponse::new);
    }

    public List<String> getAllDepartments() {
        return noticeRepository.findAll()
                .stream()
                .map(Notice::getTitle)
                .distinct()
                .toList();
    }
    public Page<NoticeDto.ListResponse> getNoticesByCategory(Category category, Pageable pageable) {
        Page<Long> idPage = noticeRepository.findIdsByCategory(category.name(), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        List<Long> ids = idPage.getContent();
        if (ids.isEmpty()) {
            return Page.empty(pageable);
        }
        List<Notice> notices = noticeRepository.findNoticesByMatchedIdsOnly(ids, PageUtils.toLatestOrder(pageable)).getContent();
        return new PageImpl<>(notices, pageable, idPage.getTotalElements()).map(NoticeDto.ListResponse::new);
    }

    public Page<NoticeDto.ListResponse> getAllNotices(Pageable pageable) {
        Page<Long> idPage = noticeRepository.findAllIds(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        List<Long> ids = idPage.getContent();
        if (ids.isEmpty()) {
            return Page.empty(pageable);
        }
        List<Notice> notices = noticeRepository.findNoticesByMatchedIdsOnly(ids, PageUtils.toLatestOrder(pageable)).getContent();
        return new PageImpl<>(notices, pageable, idPage.getTotalElements()).map(NoticeDto.ListResponse::new);
    }


    public Page<NoticeDto.ListResponse> getNoticesByIds(List<Long> ids, Pageable pageable) {
        if (ids == null || ids.isEmpty()) {
            return Page.empty(pageable);
        }
        Page<Long> idPage = noticeRepository.findIdsByIdIn(ids, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        List<Long> pageIds = idPage.getContent();
        if (pageIds.isEmpty()) {
            return Page.empty(pageable);
        }
        List<Notice> notices = noticeRepository.findNoticesByMatchedIdsOnly(pageIds, PageUtils.toLatestOrder(pageable)).getContent();
        return new PageImpl<>(notices, pageable, idPage.getTotalElements()).map(NoticeDto.ListResponse::new);
    }
    public Page<NoticeDto.ListResponse> searchInBookmarkedNotices(List<Long> ids, String keyword, Pageable pageable){
        if (ids == null || ids.isEmpty()) {
            return Page.empty(pageable);
        }
        Page<Long> idPage = noticeRepository.findIdsByIdInAndKeyword(ids, keyword, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        List<Long> pageIds = idPage.getContent();
        if (pageIds.isEmpty()) {
            return Page.empty(pageable);
        }
        List<Notice> notices = noticeRepository.findNoticesByMatchedIdsOnly(pageIds, PageUtils.toLatestOrder(pageable)).getContent();
        return new PageImpl<>(notices, pageable, idPage.getTotalElements()).map(NoticeDto.ListResponse::new);
    }

    public Page<NoticeDto.ListResponse> getAllNoticeByDepartment(List<Long> departmentId, Pageable pageable) {
        if (departmentId == null || departmentId.isEmpty()) {
            return Page.empty(pageable);
        }
        Page<Long> idPage = noticeRepository.findInternalNoticeIdsByDepartment(departmentId, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        List<Long> pageIds = idPage.getContent();
        if (pageIds.isEmpty()) {
            return Page.empty(pageable);
        }
        List<Notice> notices = noticeRepository.findNoticesByMatchedIdsOnly(pageIds, PageUtils.toLatestOrder(pageable)).getContent();
        return new PageImpl<>(notices, pageable, idPage.getTotalElements()).map(NoticeDto.ListResponse::new);
    }

    public Page<NoticeDto.ListResponse> getNoticesByDepartmentAndTargetYear(List<DeptYearBundle> bundles, Pageable pageable) {

        List<Long> requestedDeptIds = bundles.stream()
                .map(DeptYearBundle::getDepartmentId)
                .toList();

        Map<Long, String> deptIdToNameMap = departmentRepository.findAllById(requestedDeptIds).stream()
                .collect(Collectors.toMap(Department::getId, Department::getName));

        List<String> targetDeptNames = new ArrayList<>(deptIdToNameMap.values());

        List<Object[]> matchedRows = new ArrayList<>();

        // 1단계: 정확히 부합하는 학과/학년 매칭 InternalNotice ID & Date 추출
        for (DeptYearBundle bundle : bundles) {
            List<Object[]> rows = noticeRepository.findInternalNoticeIdsAndDateByExactBundle(bundle.getDepartmentId(), bundle.getTargetYear().ordinal());
            if (rows != null) {
                matchedRows.addAll(rows);
            }
        }

        // 2단계: 학과 이름에 속하는 CrawlPosts ID & Date 추출 및 결합
        if (!targetDeptNames.isEmpty()) {
            List<Object[]> rows = noticeRepository.findCrawlPostIdsAndDateBySources(targetDeptNames);
            if (rows != null) {
                matchedRows.addAll(rows);
            }
        }

        // 중복 제거 및 정렬
        Map<Long, Timestamp> uniqueNoticeMap = new HashMap<>();
        for (Object[] row : matchedRows) {
            Long id = (Long) row[0];
            Timestamp createdAt = (Timestamp) row[1];
            uniqueNoticeMap.put(id, createdAt);
        }

        List<Map.Entry<Long, Timestamp>> sortedEntries = new ArrayList<>(uniqueNoticeMap.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        int totalElements = sortedEntries.size();
        int startOffset = (int) pageable.getOffset();
        int endOffset = Math.min(startOffset + pageable.getPageSize(), totalElements);

        List<Long> pageIds = new ArrayList<>();
        if (startOffset < totalElements) {
            for (int i = startOffset; i < endOffset; i++) {
                pageIds.add(sortedEntries.get(i).getKey());
            }
        }

        if (pageIds.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Notice> notices = noticeRepository.findNoticesByMatchedIdsOnly(pageIds, PageUtils.toLatestOrder(pageable)).getContent();
        return new PageImpl<>(notices, pageable, totalElements).map(NoticeDto.ListResponse::new);
    }

    public List<Category> getCategoriesExcept(List<Category> exclusions) {
        if (exclusions == null || exclusions.isEmpty()) {
            return Arrays.asList(Category.values());
        }

        return Arrays.stream(Category.values())
                .filter(category -> !exclusions.contains(category))
                .collect(Collectors.toList());
    }

    public Resource getOgImageById(Long id, String sig) {
        String expectedSig = generateSignature(String.valueOf(id));
        log.debug("OG 이미지 서명 검증 - ID: {}", id);
        if (expectedSig == null || !expectedSig.equals(sig)) {
            log.warn("서명 불일치! ID: {}", id);
            throw new SecurityException("유효하지 않은 보안 토큰입니다.");
        }

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String dbPath = getPrimaryImagePath(notice);

        if (dbPath == null) return null;

        return loadResourceFromPath(dbPath);
    }

    // [내부 헬퍼] 이미지 경로 추출
    private String getPrimaryImagePath(Notice notice) {
        List<String> images = notice.getContentImages();

        if (images != null && !images.isEmpty()) {
            return images.get(0);
        }

        return null; // 이미지가 없는 경우
    }

    // [내부 헬퍼] 물리적 리소스 로드 (내부 파일 vs 외부 URL)
    private Resource loadResourceFromPath(String dbPath) {
        if (dbPath.startsWith("http")) {
            try { return new UrlResource(dbPath); } catch (Exception e) { return null; }
        }

        try {
            String fileName = dbPath.replace("/uploads/", "");
            Path filePath = Paths.get(UPLOAD_ROOT).resolve(fileName).normalize();
            if (!filePath.startsWith(Paths.get(UPLOAD_ROOT))) return null;

            Resource resource = new FileSystemResource(filePath);
            return (resource.exists() && resource.isReadable()) ? resource : null;
        } catch (Exception e) { return null; }
    }

    // [보안] HMAC-SHA256 서명 생성
    public String generateSignature(String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(sha256_HMAC.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("서명 생성 실패", e);
        }
    }
}

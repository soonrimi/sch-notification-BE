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
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
        Specification<Notice> spec = (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Root<InternalNotice> internalRoot = criteriaBuilder.treat(root, InternalNotice.class);
            Root<CrawlPosts> crawlRoot = criteriaBuilder.treat(root, CrawlPosts.class);
            Join<InternalNotice, Department> deptJoin = internalRoot.join("targetDept", JoinType.LEFT);

            List<Category> generalCategories = List.of(
                    Category.UNIVERSITY, Category.RECRUIT, Category.ACTIVITY, Category.PROMOTION
            );
            List<Category> targetedCategories = List.of(
                    Category.DEPARTMENT, Category.GRADE
            );

            Predicate isGeneral = root.get("category").in(generalCategories);
            Predicate isTargetCat = root.get("category").in(targetedCategories);

            Predicate matchInternalRule = criteriaBuilder.disjunction();
            for (DeptYearBundle bundle : bundles) {
                Predicate deptEq = criteriaBuilder.equal(deptJoin.get("id"), bundle.getDepartmentId());
                Predicate yearEq = criteriaBuilder.or(
                        criteriaBuilder.equal(internalRoot.get("targetYear"), bundle.getTargetYear()),
                        criteriaBuilder.equal(internalRoot.get("targetYear"), TargetYear.ALL_YEARS)
                );
                matchInternalRule = criteriaBuilder.or(matchInternalRule, criteriaBuilder.and(deptEq, yearEq));
            }
            Predicate matchCrawlRule = criteriaBuilder.disjunction();
            if (!targetDeptNames.isEmpty()) {
                matchCrawlRule = crawlRoot.get("source").in(targetDeptNames);
            }

            Predicate validTargeted = criteriaBuilder.and(
                    isTargetCat,
                    criteriaBuilder.or(matchInternalRule, matchCrawlRule)
            );

            return criteriaBuilder.or(isGeneral, validTargeted);
        };

        return noticeRepository.findAll(spec, PageUtils.toLatestOrder(pageable))
                .map(NoticeDto.ListResponse::new);
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
        Page<Notice> pages = noticeRepository.findByTitleContainingOrContentContaining(keyword, keyword, PageUtils.toLatestOrder(pageable));
            return pages.map(NoticeDto.ListResponse::new);
    }

    public List<String> getAllDepartments() {
        return noticeRepository.findAll()
                .stream()
                .map(Notice::getTitle)
                .distinct()
                .toList();
    }
    //카테고리별 공지사항 조회
    public Page<NoticeDto.ListResponse> getNoticesByCategory(Category category, Pageable pageable) {
        Page<Notice> postsPage = noticeRepository.findByCategory(category, PageUtils.toLatestOrder(pageable));
        return postsPage.map(NoticeDto.ListResponse::new);
    }

    public Page<NoticeDto.ListResponse> getAllNotices(Pageable pageable) {
        Page<Notice> postsPage = noticeRepository.findAll(PageUtils.toLatestOrder(pageable));
        return postsPage.map(NoticeDto.ListResponse::new);
    }


    public Page<NoticeDto.ListResponse> getNoticesByIds(List<Long> ids, Pageable pageable) {
        Page<Notice> postsPage = noticeRepository.findByIdInOrderByCreatedAtDesc(ids, PageUtils.toLatestOrder(pageable));
        return postsPage.map(NoticeDto.ListResponse::new);
    }
    public Page<NoticeDto.ListResponse> searchInBookmarkedNotices(List<Long> ids, String keyword, Pageable pageable){
        Page<Notice> postsPage = noticeRepository.findByIdAndTitleContainingOrContentContainingOrderByCreatedAtDescCustom(ids, keyword, PageUtils.toLatestOrder(pageable));
        return postsPage.map(NoticeDto.ListResponse::new);
    }

    public Page<NoticeDto.ListResponse> getAllNoticeByDepartment(List<Long> departmentId, Pageable pageable) {
        Page<Notice> postsPage = noticeRepository.findInternalNoticesByDepartmentOrderByCreatedAt(departmentId, PageUtils.toLatestOrder(pageable));
        return postsPage.map(NoticeDto.ListResponse::new);
    }

    public Page<NoticeDto.ListResponse> getNoticesByDepartmentAndTargetYear(List<DeptYearBundle> bundles, Pageable pageable) {

        List<Long> requestedDeptIds = bundles.stream()
                .map(DeptYearBundle::getDepartmentId)
                .toList();

        Map<Long, String> deptIdToNameMap = departmentRepository.findAllById(requestedDeptIds).stream()
                .collect(Collectors.toMap(Department::getId, Department::getName));

        List<String> targetDeptNames = new ArrayList<>(deptIdToNameMap.values());

        Specification<Notice> spec = (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Root<InternalNotice> internalRoot = criteriaBuilder.treat(root, InternalNotice.class);
            Root<CrawlPosts> crawlRoot = criteriaBuilder.treat(root, CrawlPosts.class);
            Join<InternalNotice, Department> deptJoin = internalRoot.join("targetDept", JoinType.LEFT);

            Predicate internalTotalPredicate = criteriaBuilder.disjunction();

            for (DeptYearBundle bundle : bundles) {
                Predicate deptEq = criteriaBuilder.equal(deptJoin.get("id"), bundle.getDepartmentId());
                Predicate yearEq = criteriaBuilder.equal(internalRoot.get("targetYear"), bundle.getTargetYear());

                internalTotalPredicate = criteriaBuilder.or(internalTotalPredicate, criteriaBuilder.and(deptEq, yearEq));
            }
            Predicate crawlTotalPredicate = criteriaBuilder.disjunction();
            if (!targetDeptNames.isEmpty()) {
                crawlTotalPredicate = crawlRoot.get("source").in(targetDeptNames);
            }
            return criteriaBuilder.or(internalTotalPredicate, crawlTotalPredicate);
        };
        Page<Notice> postsPage = noticeRepository.findAll(spec, PageUtils.toLatestOrder(pageable));
        return postsPage.map(NoticeDto.ListResponse::new);
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
        System.out.println("[DEBUG] 요청된 sig: " + sig);
        System.out.println("[DEBUG] 계산된 sig: " + expectedSig);
        if (expectedSig == null || !expectedSig.equals(sig)) {
            System.err.println("서명 불일치! ID: " + id + ", Expected: " + expectedSig + ", Received: " + sig);
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

package com.schnofiticationbe.service;

import com.schnofiticationbe.Utils.PageUtils;
import com.schnofiticationbe.dto.CrawlPostDto;
import com.schnofiticationbe.dto.DeptYearBundle;
import com.schnofiticationbe.dto.NoticeDto;
import com.schnofiticationbe.entity.*;
import com.schnofiticationbe.repository.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final DepartmentRepository departmentRepository;
    private final AttachmentRepository attachmentRepository;


    @Transactional
    public Page<NoticeDto.ListResponse> getCombinedNotices(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findCombinedNoticesOrderByCreatedAtDesc(PageUtils.toLatestOrder(pageable));
    return noticePage.map(NoticeDto.ListResponse::new);
    }

    public NoticeDto.DetailResponse getNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다."));

        List<Attachment> attachments = attachmentRepository.findByNoticeId(id);
        notice.setAttachments(attachments);

        notice.setViewCount(notice.getViewCount() + 1);
        noticeRepository.save(notice);

        return new NoticeDto.DetailResponse(notice);
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
}

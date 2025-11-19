package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.CrawlPostDto;
import com.schnofiticationbe.dto.DeptYearBundle;
import com.schnofiticationbe.dto.NoticeDto;
import com.schnofiticationbe.entity.*;
import com.schnofiticationbe.repository.AttachmentRepository;
import com.schnofiticationbe.repository.InternalNoticeRepository;
import com.schnofiticationbe.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.schnofiticationbe.repository.CrawlPostsRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final CrawlPostsRepository crawlPostsRepository;
    private final InternalNoticeRepository internalNoticeRepository;
    private final AttachmentRepository attachmentRepository;


    @Transactional
    public Page<NoticeDto.ListResponse> getCombinedNotices(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findCombinedNoticesOrderByCreatedAtDesc(pageable);
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
        Page<Notice> pages = noticeRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
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
        Page<Notice> postsPage = noticeRepository.findByCategory(category, pageable);
        return postsPage.map(NoticeDto.ListResponse::new);
    }

    public Page<NoticeDto.ListResponse> getAllNotices(Pageable pageable) {
        Page<Notice> postsPage = noticeRepository.findAll(pageable);
        return postsPage.map(NoticeDto.ListResponse::new);
    }


    public Page<NoticeDto.ListResponse> getNoticesByIds(List<Long> ids, Pageable pageable) {
        Page<Notice> postsPage = noticeRepository.findByIdInOrderByCreatedAtDesc(ids, pageable);
        return postsPage.map(NoticeDto.ListResponse::new);
    }
    public Page<NoticeDto.ListResponse> searchInBookmarkedNotices(List<Long> ids, String keyword, Pageable pageable){
        Page<Notice> postsPage = noticeRepository.findByIdAndTitleContainingOrContentContainingOrderByCreatedAtDescCustom(ids, keyword, pageable);
        return postsPage.map(NoticeDto.ListResponse::new);
    }

    public Page<NoticeDto.ListResponse> getAllNoticeByDepartment(List<Long> departmentId, Pageable pageable) {
        Page<Notice> postsPage = noticeRepository.findInternalNoticesByDepartmentOrderByCreatedAt(departmentId, pageable);
        return postsPage.map(NoticeDto.ListResponse::new);
    }

    public Page<NoticeDto.ListResponse> getNoticesByDepartmentAndTargetYear(List<DeptYearBundle> bundles, Pageable pageable) {
        Specification<Notice>spec=(root, query, criteriaBuilder) -> {
            var predicate=criteriaBuilder.disjunction();
            for (DeptYearBundle bundle : bundles) {
                var deptPredicate=criteriaBuilder.equal(root.get("department"), bundle.getDepartmentId());
                var yearPredicate=criteriaBuilder.equal(root.get("targetYear"), bundle.getTargetYear());
                predicate = criteriaBuilder.or(predicate, criteriaBuilder.and(deptPredicate, yearPredicate));            }
            return predicate;
        };
        Page<Notice> postsPage = noticeRepository.findAll(spec, pageable);
        return postsPage.map(NoticeDto.ListResponse::new);
    }
}

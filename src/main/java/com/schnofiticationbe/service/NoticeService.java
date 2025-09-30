package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.CrawlPostDto;
import com.schnofiticationbe.dto.NoticeDto;
import com.schnofiticationbe.entity.*;
import com.schnofiticationbe.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor

public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public Page<NoticeDto.ListResponse> getCombinedNotices(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findCombinedNoticesOrderByCreatedAtDesc(pageable);
    return noticePage.map(NoticeDto.ListResponse::new);
    }

    // 단일 공지 조회
    @Transactional
    public NoticeDto.DetailResponse getNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다."));

        notice.setViewCount(notice.getViewCount() + 1); // 조회수 증가
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

    public Page<NoticeDto.ListResponse> getAllNoticeByDepartment(Long departmentId, Pageable pageable) {
        Page<Notice> postsPage = noticeRepository.findInternalNoticesByDepartmentOrderByCreatedAt(departmentId, pageable);
        return postsPage.map(NoticeDto.ListResponse::new);
    }

    public Page<NoticeDto.ListResponse> getNoticesByDepartmentAndTargetYear(Long departmentId, TargetYear targetYear, Pageable pageable) {
        Page<Notice> postsPage = noticeRepository.findInternalNoticesByDepartmentAndYearOrderByCreatedAt(departmentId, targetYear, pageable);
        return postsPage.map(NoticeDto.ListResponse::new);
    }
}

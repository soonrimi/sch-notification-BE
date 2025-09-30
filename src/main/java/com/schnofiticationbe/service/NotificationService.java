package com.schnofiticationbe.service;

import com.schnofiticationbe.entity.Subscribe;
import com.schnofiticationbe.repository.SubscribeRepository;
import com.schnofiticationbe.Utils.KeywordMatcher;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NotificationService {
    private final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final SubscribeRepository subscribeRepository;


    public void notifySubscribers(String title, String body, String category, String targetDept, Integer targetYear) {
        List<Subscribe> all = subscribeRepository.findAll();

        String contentForMatch = ((title == null ? "" : title) + " " + (body == null ? "" : body)).trim();

        List<Subscribe> matched = all.stream()
                .filter(s -> matchByCategory(s, category))
                .filter(s -> matchByDepartmentAndYear(s, targetDept, targetYear))
                .filter(s -> matchByKeywords(s, contentForMatch))
                .collect(Collectors.toList());

        log.info("알림 매칭된 구독수: {}", matched.size());
        for (Subscribe s : matched) {

            sendPushMock(s, title, body, category);
        }
    }

    private boolean matchByCategory(Subscribe s, String noticeCategory) {

        if (s.getCategory() == null || s.getCategory().isBlank()) return true;
        if (noticeCategory == null) return false;
        return s.getCategory().equalsIgnoreCase(noticeCategory);
    }

    private boolean matchByDepartmentAndYear(Subscribe s, String targetDept, Integer targetYear) {

        if (s.getDepartment() != null && !s.getDepartment().isBlank()) {
            if (targetDept == null || targetDept.isBlank()) {

            } else {
                if (!s.getDepartment().equalsIgnoreCase(targetDept)) return false;
            }
        }

        if (s.getYear() != null) {
            if (targetYear == null) return false;
            if (!s.getYear().equals(targetYear)) return false;
        }
        return true;
    }

    private boolean matchByKeywords(Subscribe s, String content) {

        if (!KeywordMatcher.containsNoneKeyword(content, s.getExcludeKeywords())) {
            return false;
        }


        if (s.getIncludeKeywords() != null && !s.getIncludeKeywords().isEmpty()) {
            return KeywordMatcher.containsAnyKeyword(content, s.getIncludeKeywords());
        }


        return true;
    }

    private void sendPushMock(Subscribe s, String title, String body, String category) {

        log.info("[MOCK SEND] to device={} | title='{}' | category='{}' | subscriberId={}",
                s.getDevice(), title, category, s.getId());
    }
}

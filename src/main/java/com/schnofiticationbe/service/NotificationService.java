package com.schnofiticationbe.service;

import com.schnofiticationbe.entity.KeywordNotification;
import com.schnofiticationbe.entity.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KeywordService keywordService;
    private final UserProfileService userProfileService;

    /**
     * 공지 내용(content)을 입력받아서, 키워드와 사용자 정보를 기준으로 알림 대상 필터링 예시
     */
    public void sendNotificationToEligibleUsers(String content, String targetDept, Integer targetGrade) {
        List<UserProfile> allUsers = userProfileService.getAll();
        List<KeywordNotification> allKeywords = keywordService.getAll();

        for (UserProfile user : allUsers) {
            // 학과 필터
            if (targetDept != null && !user.getDepartment().equalsIgnoreCase(targetDept)) {
                continue;
            }

            // 학년 필터
            if (targetGrade != null && !user.getGrade().equals(targetGrade)) {
                continue;
            }

            // 사용자별 키워드 매칭
            for (KeywordNotification keyword : allKeywords) {
                if (isKeywordMatch(content, keyword)) {
                    // 실제 FCM 발송 등 로직 작성 가능
                    System.out.println("알림 전송 대상: " + user.getDepartment() + " " + user.getGrade() + "학년");
                }
            }
        }
    }

    private boolean isKeywordMatch(String content, KeywordNotification k) {
        // 제외 키워드 포함되어 있으면 false
        for (String exclude : k.getExcludeKeywords()) {
            if (content.contains(exclude)) return false;
        }

        // 포함 키워드 중 하나라도 포함되어 있으면 true
        if (k.getIncludeKeywords() == null || k.getIncludeKeywords().isEmpty()) return false;
        for (String include : k.getIncludeKeywords()) {
            if (content.contains(include)) return true;
        }

        return false;
    }
}

package com.schnofiticationbe.service;

import com.schnofiticationbe.Utils.KeywordMatcher;
import com.schnofiticationbe.entity.KeywordNotification;
import com.schnofiticationbe.entity.Subscribe;
import com.schnofiticationbe.entity.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KeywordService keywordService;
    private final UserProfileService userProfileService;
    private final SubscribeService subscribeService;
    private final FcmService fcmService;

    public void sendNotificationToEligibleUsers(String content, String targetDept, Integer targetGrade) {
        List<UserProfile> allUsers = userProfileService.getAll();
        List<KeywordNotification> allKeywords = keywordService.getAll();

        for (UserProfile user : allUsers) {
            if (targetDept != null && !user.getDepartment().equalsIgnoreCase(targetDept)) {
                continue;
            }

            if (targetGrade != null && !user.getGrade().equals(targetGrade)) {
                continue;
            }

            for (KeywordNotification keyword : allKeywords) {
                if (isKeywordMatch(content, keyword)) {
                    System.out.println("알림 전송 대상: " + user.getDepartment() + " " + user.getGrade() + "학년");
                }
            }
        }
    }

    public void sendKeywordNotification(String content, String title) {
        List<UserProfile> allUsers = userProfileService.getAll();

        for (UserProfile user : allUsers) {
            String device = user.getDevice();
            if (device == null || device.isBlank()) {
                continue;
            }

            List<Subscribe> subscribes = subscribeService.getSubscribesByDevice(device);
            boolean isSubscribed = subscribes.stream()
                    .anyMatch(Subscribe::isSubscribed);

            if (!isSubscribed) {
                continue;
            }

            List<KeywordNotification> userKeywords = keywordService.getByDeviceId(device);

            for (KeywordNotification keyword : userKeywords) {
                if (isKeywordMatch(content, keyword)) {
                    String notificationTitle = title != null ? title : "키워드 알림";
                    String notificationBody = content.length() > 100 
                            ? content.substring(0, 100) + "..." 
                            : content;
                    fcmService.sendPushNotification(device, notificationTitle, notificationBody);
                    break;
                }
            }
        }
    }

    private boolean isKeywordMatch(String content, KeywordNotification k) {
        if (content == null || content.isBlank()) {
            return false;
        }

        if (!KeywordMatcher.containsNoneKeyword(content, k.getExcludeKeywords())) {
            return false;
        }

        if (k.getIncludeKeywords() == null || k.getIncludeKeywords().isEmpty()) {
            return false;
        }

        return KeywordMatcher.containsAnyKeyword(content, k.getIncludeKeywords());
    }
}

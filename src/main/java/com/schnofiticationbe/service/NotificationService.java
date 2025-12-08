package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.NotificationDto;
import com.schnofiticationbe.entity.KeywordNotification;
import com.schnofiticationbe.entity.Subscribe;
import com.schnofiticationbe.repository.KeywordRepository;
import com.schnofiticationbe.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SubscribeRepository subscribeRepository;
    private final KeywordRepository keywordRepository;

    public NotificationDto.SendSummary processNewPost(NotificationDto.NewPostRequest request) {

        String searchText = buildSearchText(request.getTitle(), request.getContent());

        // 1. 카테고리 구독 매칭
        List<Subscribe> categorySubscribes = Collections.emptyList();
        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            categorySubscribes = subscribeRepository.findByCategoryAndSubscribedTrue(request.getCategory());
        }

        // 2. 키워드 구독 매칭
        List<KeywordNotification> keywordNotifications = keywordRepository.findAll();

        Set<String> targetDevices = new HashSet<>();

        // 2-1. 카테고리 구독으로 매칭된 디바이스에 알림
        for (Subscribe subscribe : categorySubscribes) {
            String device = subscribe.getDevice();
            if (device != null && !device.isBlank()) {
                targetDevices.add(device);
                sendToDevice(device, request.getTitle(), request.getUrl());
            }
        }

        // 2-2. 키워드 구독으로 매칭된 디바이스에 알림
        int keywordMatchCount = 0;
        for (KeywordNotification keywordNotification : keywordNotifications) {
            if (isMatchByKeyword(keywordNotification, searchText)) {
                String device = keywordNotification.getDevice();
                if (device != null && !device.isBlank()) {
                    targetDevices.add(device);
                    keywordMatchCount++;
                    sendToDevice(device, request.getTitle(), request.getUrl());
                }
            }
        }

        NotificationDto.SendSummary summary = new NotificationDto.SendSummary();
        summary.setCategoryMatchCount(categorySubscribes.size());
        summary.setKeywordMatchCount(keywordMatchCount);
        summary.setTotalDeviceCount(targetDevices.size());
        summary.setTargetDevices(targetDevices);

        return summary;
    }

    private String buildSearchText(String title, String content) {
        StringBuilder sb = new StringBuilder();
        if (title != null) {
            sb.append(title).append(" ");
        }
        if (content != null) {
            sb.append(content);
        }
        return sb.toString();
    }

    private boolean isMatchByKeyword(KeywordNotification keywordNotification, String text) {
        String lowered = text.toLowerCase(Locale.ROOT);

        List<String> includes = keywordNotification.getIncludeKeywords();
        if (includes != null && !includes.isEmpty()) {
            boolean anyIncluded = includes.stream()
                    .filter(Objects::nonNull)
                    .map(s -> s.toLowerCase(Locale.ROOT))
                    .anyMatch(lowered::contains);
            if (!anyIncluded) {
                return false;
            }
        }

        List<String> excludes = keywordNotification.getExcludeKeywords();
        if (excludes != null && !excludes.isEmpty()) {
            boolean anyExcluded = excludes.stream()
                    .filter(Objects::nonNull)
                    .map(s -> s.toLowerCase(Locale.ROOT))
                    .anyMatch(lowered::contains);
            if (anyExcluded) {
                return false;
            }
        }

        return true;
    }

    private void sendToDevice(String deviceId, String title, String url) {
        // TODO: 실제 푸시 발송(Firebase Cloud Messaging 등)으로 교체하면 됨
        System.out.println("[NOTIFY] device=" + deviceId + " title=" + title + " url=" + url);
    }
}

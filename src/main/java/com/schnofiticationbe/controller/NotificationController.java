package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.NotificationDto;
import com.schnofiticationbe.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/posts")
    public ResponseEntity<NotificationDto.SendSummary> handleNewPost(
            @RequestBody NotificationDto.NewPostRequest request) {

        NotificationDto.SendSummary summary = notificationService.processNewPost(request);
        return ResponseEntity.ok(summary);
    }
}

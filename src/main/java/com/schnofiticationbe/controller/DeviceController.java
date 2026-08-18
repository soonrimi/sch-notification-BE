package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.DeviceDto;
import com.schnofiticationbe.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * 클라이언트 디바이스 등록 엔드포인트
     *
     * 요청 바디 예시:
     * {
     *   "deviceToken": "fcm_or_apns_token",
     *   "os": "ANDROID",
     *   "appVersion": "1.0.0"
     * }
     *
     * 응답 예시:
     * {
     *   "deviceId": "server-generated-uuid",
     *   "deviceToken": "...",
     *   "os": "ANDROID",
     *   "appVersion": "1.0.0",
     *   "createdAt": "2026-01-28T12:34:56"
     * }
     */
    @PostMapping
    public ResponseEntity<DeviceDto.RegisterResponse> register(
            @RequestBody DeviceDto.RegisterRequest request
    ) {
        DeviceDto.RegisterResponse response = deviceService.register(request);
        return ResponseEntity.ok(response);
    }
}


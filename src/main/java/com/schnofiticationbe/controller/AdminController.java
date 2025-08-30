package com.schnofiticationbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.schnofiticationbe.dto.AdminDto;
import com.schnofiticationbe.dto.InternalNoticeDto;
import com.schnofiticationbe.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<AdminDto.SignupResponse> register(@RequestBody AdminDto.SignupRequest req) {
        return ResponseEntity.ok(adminService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AdminDto.LoginResponse> login(@RequestBody AdminDto.LoginRequest req) {
        return ResponseEntity.ok(adminService.login(req));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AdminDto.ResetPasswordResponse> resetPassword(
            @RequestBody AdminDto.ResetPasswordRequest req) {
        return ResponseEntity.ok(adminService.resetPassword(req));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody Map<String, String> req) {
        String userId = req.get("userId");
        String tempPassword = req.get("tempPassword");
        String newPassword = req.get("newPassword");

        adminService.changePassword(userId, tempPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notice")
    public ResponseEntity<InternalNoticeDto.Response> createNotice(
            @RequestHeader("Authorization") String authorization,
            @RequestPart("notice") String noticeJson,
            @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InternalNoticeDto.CreateRequest req = mapper.readValue(noticeJson, InternalNoticeDto.CreateRequest.class);
            return ResponseEntity.ok(adminService.createInternalNotice(
                authorization, req, files
            ));
        } catch (Exception e) {
            throw new RuntimeException("공지 등록 실패: JSON 파싱 오류", e);
        }
    }

    @GetMapping("/myNotices")
    public ResponseEntity<List<InternalNoticeDto.Response>> getMyNotices(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(adminService.getMyInternalNotice(authorization));
    }

}

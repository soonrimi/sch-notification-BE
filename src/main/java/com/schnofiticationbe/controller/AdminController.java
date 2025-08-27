package com.schnofiticationbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.schnofiticationbe.dto.AdminDto;
import com.schnofiticationbe.dto.NoticeDto;
import com.schnofiticationbe.service.AdminService;
import com.schnofiticationbe.service.NoticeService;
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
    private final NoticeService noticeService;

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

//    @PostMapping("/reset-password")
//    public ResponseEntity<Void> resetPassword(@RequestBody Map<String, String> req) {
//        String username = req.get("username");
//        String tempPassword = RandomStringUtils.randomAlphanumeric(10); // Apache Commons Lang
//        adminService.updatePassword(username, tempPassword);
//
//        // 이메일 발송 : username이 이메일이면 그대로 사용하면 됨
//        emailService.sendTempPassword(username, tempPassword);
//
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String tempPassword = req.get("tempPassword");
        String newPassword = req.get("newPassword");

        adminService.changePassword(username, tempPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notice")
    public ResponseEntity<NoticeDto.Response> createNotice(
            @RequestParam Long adminId,
            @RequestPart("notice") String noticeJson,
            @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            NoticeDto.CreateRequest req = mapper.readValue(noticeJson, NoticeDto.CreateRequest.class);
            return ResponseEntity.ok(noticeService.createNotice(adminId, req, files));
        } catch (Exception e) {
            throw new RuntimeException("공지 등록 실패: JSON 파싱 오류", e);
        }
    }

}

package com.schnofiticationbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.schnofiticationbe.dto.AdminDto;
import com.schnofiticationbe.dto.InternalNoticeDto;
import com.schnofiticationbe.entity.Department;
import com.schnofiticationbe.service.AdminService;
import com.schnofiticationbe.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<AdminDto.SignupResponse> register(@RequestBody AdminDto.SignupRequest req) {
        return ResponseEntity.ok(adminService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AdminDto.LoginResponse> login(@RequestBody AdminDto.LoginRequest req) {
        return ResponseEntity.ok(adminService.login(req));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AdminDto.MessageResponse> resetPassword(
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

    @PostMapping(value = "/internal-notice", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InternalNoticeDto.InternalNoticeListResponse> createInternalNotice(
            @RequestHeader("Authorization") String authorization,
            @RequestPart("internalNotice") InternalNoticeDto.CreateInternalNoticeRequest req,
            @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) {
        return ResponseEntity.ok(adminService.createInternalNotice(authorization, req, files));
    }

    @GetMapping("/my-notices")
    public ResponseEntity<List<InternalNoticeDto.InternalNoticeListResponse>> getMyNotices(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(adminService.getMyInternalNotice(authorization));
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartment() {
        return ResponseEntity.ok(adminService.getAllDepartment());
    }

    @GetMapping("/list")
    public ResponseEntity<List<AdminDto.AdminUserResponse>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<AdminDto.AdminUserResponse> updateAdmin(
            @PathVariable Long adminId,
            @RequestBody AdminDto.AdminUpdateRequest req
    ) {
        return ResponseEntity.ok(adminService.updateAdmin(adminId, req));
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<AdminDto.MessageResponse> deleteAdmin(
            @PathVariable Long adminId,
            @RequestBody AdminDto.AdminDeleteRequest req
    ) {
        return ResponseEntity.ok(adminService.deleteAdmin(adminId, req));
    }

    @PostMapping("/send-verification")
    public ResponseEntity<Void> sendVerification(@RequestParam String userId) {
        adminService.sendVerificationMail(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(
            @RequestParam("userId") String userId,
            @RequestParam("token") String token) {

        if (emailService.verifyToken(userId, token)) {
            emailService.markVerified(userId);

            return ResponseEntity.ok("이메일 인증 완료!");
        } else {
            return ResponseEntity.badRequest().body("잘못된 토큰이거나 만료됨");
        }
    }

    // 새 로그인 api (기존 /login 사용X)
    @PostMapping("/login-email")
    public ResponseEntity<String> loginEmail(@RequestBody AdminDto.EmailLoginRequest req) {
        return ResponseEntity.ok(adminService.loginWithEmailOtp(req));
    }

    @PostMapping("/otp")
    public ResponseEntity<AdminDto.LoginResponse> verifyOtp(@RequestBody AdminDto.OtpVerifyRequest req) {
        return ResponseEntity.ok(adminService.verifyEmailOtp(req));
    }
}

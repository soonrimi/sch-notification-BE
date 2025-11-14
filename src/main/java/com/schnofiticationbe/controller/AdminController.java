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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping("/send-verification")
    public ResponseEntity<Void> sendVerification(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        adminService.resendVerificationMail(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String userId, @RequestParam String token) {
        adminService.verifyEmail(userId, token);

        String html = """
        <!DOCTYPE html>
        <html lang="ko">
        <head>
            <meta charset="UTF-8">
            <title>인증 완료</title>
            <style>
                body { 
                    font-family: Arial, sans-serif;
                    text-align: center; 
                    margin-top: 120px;
                }
                .box {
                    display: inline-block;
                    padding: 30px 40px;
                    border-radius: 12px;
                    box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                    background: #ffffff;
                }
                h2 { color: #2e7dff; }
                #countdown {
                    font-size: 24px;
                    margin-top: 10px;
                    color: #333;
                    font-weight: bold;
                }
            </style>
            <script>
                let timeLeft = 5;

                const timer = setInterval(() => {
                    document.getElementById("countdown").innerText = timeLeft;
                    timeLeft--;

                    if (timeLeft < 0) {
                        clearInterval(timer);
                        window.location.href = '/login';
                    }
                }, 1000);
            </script>
        </head>
        <body>
            <div class="box">
                <h2>이메일 인증 완료!</h2>
                <p>곧 로그인 페이지로 이동합니다.</p>
                <div id="countdown">5</div>
            </div>
        </body>
        </html>
        """;

        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(html);
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginEmail(@RequestBody AdminDto.EmailLoginRequest req) {
        return ResponseEntity.ok(adminService.loginWithEmailOtp(req));
    }

    @PostMapping("/otp")
    public ResponseEntity<AdminDto.LoginResponse> verifyOtp(@RequestBody AdminDto.OtpVerifyRequest req) {
        return ResponseEntity.ok(adminService.verifyEmailOtp(req));
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
            @RequestHeader ("Authorization") String authorization,
            @RequestPart("internalNotice") InternalNoticeDto.CreateInternalNoticeRequest req,
            @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(adminService.createInternalNotice(authentication, req, files));
    }

    @GetMapping("/my-notices")
    public ResponseEntity<List<InternalNoticeDto.InternalNoticeListResponse>> getMyNotices(
            @RequestHeader ("Authorization") String authorization
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(adminService.getMyInternalNotice(authentication));
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
}

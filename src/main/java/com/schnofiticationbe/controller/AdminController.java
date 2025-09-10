package com.schnofiticationbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.schnofiticationbe.dto.AdminDto;
import com.schnofiticationbe.dto.InternalNoticeDto;
import com.schnofiticationbe.entity.Department;
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

    @PostMapping(value = "/internal-notice", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InternalNoticeDto.InternalNoticeResponse> createInternalNotice(
            @RequestHeader("Authorization") String authorization,
            @RequestPart("internalNotice") InternalNoticeDto.CreateRequest req,
            @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) {
        return ResponseEntity.ok(adminService.createInternalNotice(authorization, req, files));
    }

    @GetMapping("/my-notices")
    public ResponseEntity<List<InternalNoticeDto.InternalNoticeResponse>> getMyNotices(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(adminService.getMyInternalNotice(authorization));
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartment() {
        return ResponseEntity.ok(adminService.getAllDepartment());
    }

    @GetMapping("/list")
    public ResponseEntity<List<AdminDto.MyInfoResponse>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<AdminDto.MyInfoResponse> updateAdmin(
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

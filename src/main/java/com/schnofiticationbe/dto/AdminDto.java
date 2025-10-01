package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Admin;
import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.entity.Department;
import com.schnofiticationbe.entity.TargetYear;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

public class AdminDto {

    // 회원가입 요청 DTO
    @Getter
    @Setter
    @Schema(requiredProperties = {"userId", "password", "name", "affiliation", "registerPassword"})
    public static class SignupRequest {
        private String userId;
        private String password;
        private String name;
        private Admin.Affiliation affiliation;
        private String registerPassword;
        private Set<String> categories;
        private Set<Long> departmentIds;
        private Set<String> grades;
    }

    // 회원가입 응답 DTO
    @Getter @Setter
    @Schema(requiredProperties = {"id", "userId", "name", "affiliation"})
    public static class SignupResponse {
        private Long id;
        private String userId;
        private String name;
        private Admin.Affiliation affiliation;

        public SignupResponse(Long id, String userId, String name, Admin.Affiliation affiliation) {
            this.id = id;
            this.userId = userId;
            this.name = name;
            this.affiliation = affiliation;
        }
    }

    // 로그인 요청 DTO
    @Getter
    @Setter
    @Schema(requiredProperties = {"userId", "password"})
    public static class LoginRequest {
        private String userId;
        private String password;
    }

    // 로그인 응답 DTO
    @Getter
    @Setter
    @Schema(requiredProperties = {"userId", "name", "message", "token"})
    public static class LoginResponse {
        private String userId;
        private String name;
        private String message;
        private String token;

        public LoginResponse(String userId, String name, String message, String token) {
            this.userId = userId;
            this.name = name;
            this.message = message;
            this.token = token;
        }
    }

    @Getter @Setter
    public static class EmailLoginRequest {
        private String userId;
        private String password;
    }

    @Getter @Setter
    public static class OtpVerifyRequest {   
        private String userId;
        private int otp;
    }


    @Getter @Setter
    @Schema(requiredProperties = {"userId", "registerPassword","tempPassword"})
    public static class ResetPasswordRequest {
        private String userId; // 아이디
        private String registerPassword; // 보안 비밀번호
        private String newPassword; // 새 비밀번호
    }

    @Getter
    @Setter
    @Schema(requiredProperties = {"id", "userId", "name", "affiliation", "role"})
    public static class AdminUserResponse {
        private Long id;
        private String userId;
        private String name;
        private Admin.Affiliation affiliation;
        private Set<Category> categories;
        private Set<Department> departments;
        private Set<TargetYear> grades;

        public AdminUserResponse(Admin admin) {
            this.id = admin.getId();
            this.userId = admin.getUserId();
            this.name = admin.getName();
            this.affiliation = admin.getAffiliation();
            this.categories = admin.getCategories();
            this.departments = admin.getDepartments();
            this.grades = admin.getGrades();
        }
    }

    @Getter @Setter
    @Schema(requiredProperties = {"name", "affiliation", "categories", "departmentIds", "grades", "registerPassword"})
    public static class AdminUpdateRequest {
        private String name;
        private Admin.Affiliation affiliation;
        private String password;
        private String registerPassword; // 보안 비밀번호
        private Set<String> categories;
        private Set<Long> departmentIds;
        private Set<String> grades;
    }

    @Getter @Setter
    @Schema(requiredProperties = {"registerPassword"})
    public static class AdminDeleteRequest {
        private String registerPassword; // 보안 비밀번호
    }

    @Getter
    @AllArgsConstructor
    @Schema(requiredProperties = {"message"})
    public static class MessageResponse {
        private String message;
    }

}

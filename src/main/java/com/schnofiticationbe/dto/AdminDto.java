package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Admin;
import com.schnofiticationbe.entity.InternalNotice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class AdminDto {

    // 회원가입 요청 DTO
    @Getter
    @Setter
    public static class SignupRequest {
        private String userId;
        private String password;
        private String name;
        private String affiliation;
        private String registerPassword;
    }

    // 회원가입 응답 DTO
    @Getter @Setter
    public static class SignupResponse {
        private Long id;
        private String userId;
        private String name;
        private String affiliation;

        public SignupResponse(Long id, String userId, String name, String affiliation) {
            this.id = id;
            this.userId = userId;
            this.name = name;
            this.affiliation = affiliation;
        }
    }

    // 로그인 요청 DTO
    @Getter
    @Setter
    public static class LoginRequest {
        private String userId;
        private String password;
    }

    // 로그인 응답 DTO
    @Getter
    @Setter
    public static class LoginResponse {
        private String userId;
        private String name;
        private Admin.Role role;
        private String message;
        private String token;

        public LoginResponse(String userId, String name, Admin.Role role, String message, String token) {
            this.userId = userId;
            this.name = name;
            this.role = role;
            this.message = message;
            this.token = token;
        }
    }

    @Getter @Setter
    public static class ResetPasswordRequest {
        private String userId; // 아이디
    }

    @Getter @Setter
    public static class ResetPasswordResponse {
        private String userId;
        private String tempPassword;

        public ResetPasswordResponse(String userId, String tempPassword) {
            this.userId = userId;
            this.tempPassword = tempPassword;
        }
    }

    public record MyInfoResponse(
            String name,
            String affiliation
    ) {
        public MyInfoResponse(Admin admin) {
            this(
                    admin.getName(),
                    admin.getAffiliation()
            );
        }
    }

    @Getter @Setter
    public static class UpdateRequest {
        private String name;
        private String affiliation;
        private String password;
        private String registerPassword;
    }

    @Getter @Setter
    public static class DeleteRequest {
        private String registerPassword;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DeleteResponse {
        private String message;
    }
}

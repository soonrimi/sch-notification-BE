package conconccc.schnofiticationbe.dto;

import lombok.Getter;
import lombok.Setter;

public class AdminDto {

    // 회원가입 요청 DTO
    @Getter
    @Setter
    public static class SignupRequest {
        private String username;
        private String password;
        private String name;
        private String role;
        private String registerPassword;
    }

    // 회원가입 응답 DTO
    @Getter @Setter
    public static class SignupResponse {
        private Long id;
        private String username;
        private String name;
        private String role;

        public SignupResponse(Long id, String username, String name, String role) {
            this.id = id;
            this.username = username;
            this.name = name;
            this.role = role;
        }
    }

    // 로그인 요청 DTO
    @Getter
    @Setter
    public static class LoginRequest {
        private String username;
        private String password;
    }

    // 로그인 응답 DTO
    @Getter
    @Setter
    public static class LoginResponse {
        private String username;
        private String name;
        private String role;
        private String message;
        private String token;

        public LoginResponse(String username, String name, String role, String message, String token) {
            this.username = username;
            this.name = name;
            this.role = role;
            this.message = message;
            this.token = token;
        }
    }

    @Getter @Setter
    public static class ResetPasswordRequest {
        private String username; // 아이디
    }

    @Getter @Setter
    public static class ResetPasswordResponse {
        private String username;
        private String tempPassword;

        public ResetPasswordResponse(String username, String tempPassword) {
            this.username = username;
            this.tempPassword = tempPassword;
        }
    }

}

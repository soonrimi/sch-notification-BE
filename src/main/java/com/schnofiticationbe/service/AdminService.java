package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.AdminDto;
import com.schnofiticationbe.entity.Admin;
import com.schnofiticationbe.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_REGISTER_PASSWORD}")
    private String adminRegisterPassword;

    public AdminDto.SignupResponse register(AdminDto.SignupRequest req) {
        if (adminRepository.existsByUsername(req.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다.");
        }

        if (!req.getRegisterPassword().equals(adminRegisterPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 환경변수와 일치하지 않습니다.");
        }

        Admin admin = new Admin();
        admin.setUsername(req.getUsername());
        admin.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        admin.setName(req.getName());
        admin.setRole(req.getRole());

        Admin saved = adminRepository.save(admin);

        return new AdminDto.SignupResponse(saved.getId(), saved.getUsername(), saved.getName(), saved.getRole());
    }

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public AdminDto.LoginResponse login(AdminDto.LoginRequest req) {
    Admin admin = adminRepository.findByUsername(req.getUsername())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다."));

    if (!passwordEncoder.matches(req.getPassword(), admin.getPasswordHash())) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다.");
    }

    // JWT 토큰 생성
    Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    String token = Jwts.builder()
        .setSubject(admin.getUsername())
        .claim("role", admin.getRole())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1일
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();

    return new AdminDto.LoginResponse(
        admin.getUsername(),
        admin.getName(),
        admin.getRole(),
        "로그인 성공",
        token
    );
    }

    public AdminDto.ResetPasswordResponse resetPassword(AdminDto.ResetPasswordRequest req) {
        Admin admin = adminRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 아이디가 존재하지 않습니다."));

        // 임시 비밀번호 생성 (8자리 랜덤)
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        // 암호화 저장
        admin.setPasswordHash(passwordEncoder.encode(tempPassword));
        adminRepository.save(admin);

        // 임시 비밀번호 반환
        return new AdminDto.ResetPasswordResponse(admin.getUsername(), tempPassword);
    }

//    public void updatePassword(String username, String rawPassword) {
//        Admin admin = adminRepository.findByUsername(username)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 계정"));
//        admin.setPasswordHash(passwordEncoder.encode(rawPassword));
//        adminRepository.save(admin);
//    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 계정"));

        if (!passwordEncoder.matches(oldPassword, admin.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "임시 비밀번호가 일치하지 않습니다.");
        }

        admin.setPasswordHash(passwordEncoder.encode(newPassword));
        adminRepository.save(admin);
    }

    public Admin findById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다."
                ));
    }

}
package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.AdminDto;
import com.schnofiticationbe.entity.Department;
import com.schnofiticationbe.repository.InternalNoticeRepository;
import com.schnofiticationbe.repository.LessonRepository;
import com.schnofiticationbe.security.jwt.JwtProvider;
import com.schnofiticationbe.dto.InternalNoticeDto;
import com.schnofiticationbe.entity.Admin;
import com.schnofiticationbe.entity.InternalNotice;
import com.schnofiticationbe.entity.Attachment;
import com.schnofiticationbe.repository.AdminRepository;
import com.schnofiticationbe.repository.AttachmentRepository;
import com.schnofiticationbe.Utils.StoreAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class AdminService {
    private final JwtProvider jwtProvider;
    private final AdminRepository adminRepository;
    private final InternalNoticeRepository internalNoticeRepository;
    private final AttachmentRepository attachmentRepository;
    private final StoreAttachment storeAttachment;
    private final PasswordEncoder passwordEncoder;
    private final LessonRepository lessonRepository;

    // 공지 생성 (InternalNotice)
    public InternalNoticeDto.InternalNoticeResponse createInternalNotice(String jwtToken, InternalNoticeDto.CreateRequest req, List<MultipartFile> files) {
        String userId = jwtProvider.getUserId(jwtToken);
        Admin admin = adminRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없습니다."));

        Department department = lessonRepository.findById(req.getTargetDept())
                .orElseThrow(() -> new IllegalArgumentException("해당 학과 정보가 존재하지 않습니다."));

        InternalNotice notice = new InternalNotice();
        notice.setTitle(req.getTitle());
        notice.setContent(req.getContent());
        notice.setWriter(admin);
        notice.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        notice.setViewCount(0);
        notice.setTargetYear(req.getTargetYear());
        notice.setTargetDept(department);

        InternalNotice savedNotice = internalNoticeRepository.save(notice);

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileUrl = storeAttachment.saveFile(file);
                    Attachment attachment = new Attachment();
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setFileUrl(fileUrl);
                    attachment.setNotice(savedNotice);
                    savedNotice.getAttachments().add(attachment);
                    attachmentRepository.save(attachment);
                }
            }
        }
        return new InternalNoticeDto.InternalNoticeResponse(savedNotice);
    }

    @Value("${ADMIN_REGISTER_PASSWORD}")
    private String adminRegisterPassword;

    public AdminDto.SignupResponse register(AdminDto.SignupRequest req) {
        if (adminRepository.existsByUserId(req.getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다.");
        }

        if (!req.getRegisterPassword().equals(adminRegisterPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 환경변수와 일치하지 않습니다.");
        }

        Admin admin = new Admin();
        admin.setUserId(req.getUserId());
        admin.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        admin.setName(req.getName());
        admin.setRole(Admin.Role.ADMIN);
        admin.setAffiliation(req.getAffiliation());

        Admin saved = adminRepository.save(admin);

        return new AdminDto.SignupResponse(saved.getId(), saved.getUserId(), saved.getName(), saved.getAffiliation());
    }

    public AdminDto.LoginResponse login(AdminDto.LoginRequest req) {
        Admin admin = adminRepository.findByUserId(req.getUserId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다."));


        if (!passwordEncoder.matches(req.getPassword(), admin.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다.");
        }

        // JWT 토큰 생성 (JwtProvider 사용)
        String token = jwtProvider.createToken(admin.getUserId(), admin.getRole());

        return new AdminDto.LoginResponse(
            admin.getUserId(),
            admin.getName(),
            admin.getRole(),
            "로그인 성공",
            token
        );
    }

    public AdminDto.ResetPasswordResponse resetPassword(AdminDto.ResetPasswordRequest req) {
        Admin admin = adminRepository.findByUserId(req.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 아이디가 존재하지 않습니다."));

        // 임시 비밀번호 생성 (8자리 랜덤)
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        // 암호화 저장
        admin.setPasswordHash(passwordEncoder.encode(tempPassword));
        adminRepository.save(admin);

        // 임시 비밀번호 반환
        return new AdminDto.ResetPasswordResponse(admin.getUserId(), tempPassword);
    }

//    public void updatePassword(String userId, String rawPassword) {
//        Admin admin = adminRepository.findByUsername(userId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 계정"));
//        admin.setPasswordHash(passwordEncoder.encode(rawPassword));
//        adminRepository.save(admin);
//    }

    public void changePassword(String userId, String oldPassword, String newPassword) {
        Admin admin = adminRepository.findByUserId(userId)
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

    public List<InternalNoticeDto.InternalNoticeResponse> getMyInternalNotice(String jwtToken) {
        String userId = jwtProvider.getUserId(jwtToken);
        Admin getCurrentAdmin = adminRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다."));

        List<InternalNotice> notices = internalNoticeRepository.findByWriter(getCurrentAdmin);
        return notices.stream().map(InternalNoticeDto.InternalNoticeResponse::new).toList();
    }

    public List<Department> getAllDepartment() {
        return lessonRepository.findAll().stream().toList();
    }

    public AdminDto.MyInfoResponse getMyInfo(String jwtToken) {
        String userId = jwtProvider.getUserId(jwtToken);

        Admin admin = adminRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "계정을 찾을 수 없습니다."));

        return new AdminDto.MyInfoResponse(admin);
    }

    public AdminDto.MyInfoResponse updateMyInfo(String jwtToken, AdminDto.UpdateRequest req) {
        String userId = jwtProvider.getUserId(jwtToken);

        if (!adminRegisterPassword.equals(req.getRegisterPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "보안 비밀번호가 일치하지 않습니다.");
        }

        Admin admin = adminRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "계정을 찾을 수 없습니다."));

        // 값이 들어온 항목만 업데이트
        if (req.getName() != null && !req.getName().isBlank()) {
            admin.setName(req.getName());
        }
        if (req.getAffiliation() != null && !req.getAffiliation().isBlank()) {
            admin.setAffiliation(req.getAffiliation());
        }
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            admin.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        }

        Admin saved = adminRepository.save(admin);
        return new AdminDto.MyInfoResponse(saved);
    }

    public AdminDto.DeleteResponse deleteMyInfo(String jwtToken, AdminDto.DeleteRequest req) {
        String userId = jwtProvider.getUserId(jwtToken);

        if (!adminRegisterPassword.equals(req.getRegisterPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "보안 비밀번호가 일치하지 않습니다.");
        }

        Admin admin = adminRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "계정을 찾을 수 없습니다."));

        adminRepository.delete(admin);

        return new AdminDto.DeleteResponse("계정이 삭제되었습니다.");
    }
}
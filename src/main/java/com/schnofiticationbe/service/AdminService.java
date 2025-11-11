package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.AdminDto;
import com.schnofiticationbe.entity.*;
import com.schnofiticationbe.repository.*;
import com.schnofiticationbe.security.jwt.JwtProvider;
import com.schnofiticationbe.dto.InternalNoticeDto;
import com.schnofiticationbe.Utils.StoreAttachment;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
@RequiredArgsConstructor
public class AdminService {
    private final JwtProvider jwtProvider;
    private final AdminRepository adminRepository;
    private final InternalNoticeRepository internalNoticeRepository;
    private final StoreAttachment storeAttachment;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final EmailService emailService;
    private final JavaMailSender mailSender;
    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();

    @Value("${ADMIN_REGISTER_PASSWORD}")
    private String adminRegisterPassword;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Data
    @AllArgsConstructor
    static class OtpData {
        private int code;
        private long expireAtMillis;
    }

    public String loginWithEmailOtp(AdminDto.EmailLoginRequest req) {
        Admin admin = adminRepository.findByUserId(req.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다."));

        if (!passwordEncoder.matches(req.getPassword(), admin.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다.");
        }

        // 이메일 인증 여부 확인
        if (!admin.isEmailVerified()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 인증이 완료되지 않았습니다. 이메일 인증을 먼저 해주세요.");
        }

        int otp = (int) (Math.random() * 900000) + 100000;
        long expire = System.currentTimeMillis() + 5 * 60 * 1000; // 5분 유효 설정

        otpStore.put(admin.getUserId(), new OtpData(otp, expire));

        emailService.sendOtp(admin.getUserId(), otp);

        return "이메일로 OTP를 발송했습니다. 5분 내 입력해주세요.";
    }

    public AdminDto.LoginResponse verifyEmailOtp(AdminDto.OtpVerifyRequest req) {
        OtpData data = otpStore.get(req.getUserId());
        if (data == null || System.currentTimeMillis() > data.getExpireAtMillis()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "OTP가 만료되었거나 존재하지 않습니다.");
        }
        if (data.getCode() != req.getOtp()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "OTP 코드가 올바르지 않습니다.");
        }

        otpStore.remove(req.getUserId());

        Admin admin = adminRepository.findByUserId(req.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "관리자 없음"));

        String token = jwtProvider.createToken(admin.getUserId(), admin.getAffiliation());
        return new AdminDto.LoginResponse(
                admin.getUserId(),
                admin.getName(),
                "로그인 성공",
                token
        );
    }


    // 공지 생성 (InternalNotice)
    public InternalNoticeDto.InternalNoticeListResponse createInternalNotice(Authentication jwtToken, InternalNoticeDto.CreateInternalNoticeRequest req, List<MultipartFile> files) {
        String userIdInToken=jwtToken.getName();
        Admin admin = adminRepository.findByUserId(userIdInToken)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없습니다."));

        Set<Department> departments = new HashSet<>(departmentRepository.findAllById(req.getTargetDepartmentIds()));

        InternalNotice notice = new InternalNotice();
        notice.setTitle(req.getTitle());
        notice.setContent(req.getContent());
        notice.setWriter(admin);
        notice.setViewCount(0);
        notice.setTargetYear(req.getTargetYear());
        notice.setTargetDept(departments);
        notice.setSentToKakao(false);
        notice.setCategory(req.getCategory());

        try {
            // save를 시도합니다.
            InternalNotice savedNotice = internalNoticeRepository.save(notice);

            // (성공 시 파일 처리 로직)
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String fileUrl = storeAttachment.saveFile(file);
                        Attachment attachment = new Attachment();
                        attachment.setFileName(file.getOriginalFilename());
                        attachment.setFileUrl(fileUrl);
                        savedNotice.addAttachment(attachment);
                        // savedNotice.getInternalAttachment().add(attachment); // 이 부분은 양방향 연관관계 편의 메서드에서 처리하는 것이 좋습니다.
//                        InternalAttachmentRepository.save(attachment);
                    }
                }
                savedNotice = internalNoticeRepository.save(savedNotice);
            }
            return new InternalNoticeDto.InternalNoticeListResponse(savedNotice);

        } catch (Exception e) {
            // !!!!! 오류 발생 시, 저장하려던 notice 객체의 상태를 강제로 출력합니다 !!!!!
            System.out.println("==================== SAVE FAILED: DATA DUMP ====================");
            System.out.println("Title: " + notice.getTitle());
            System.out.println("Content: " + notice.getContent());
            System.out.println("Category: " + notice.getCategory());
            System.out.println("================================================================");

            // 원래 발생한 예외를 다시 던져서 기존 흐름을 방해하지 않습니다.
            throw e;
        }
    }

    public void sendVerificationMail(String email) {
        String token = UUID.randomUUID().toString();
        emailService.saveToken(email, token);

//        String link = "http://notification.iubns.net/api/admin/verify?userId=" + email + "&token=" + token;
        String link = "http://localhost:7100/api/admin/verify?userId=" + email + "&token=" + token;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("[순천향대학교 Soonrimi] 이메일 인증");

            helper.setFrom(mailUsername);

            String htmlContent = "<h1>순천향대학교 Soonrimi</h1>"
                    + "<p>총학생회에서 당신의 계정으로 회원가입을 완료했습니다.</p>"
                    + "<p>본인 인증을 하기 위해 먼저 아래 링크로 들어가 이메일 인증을 해 주세요.</p>"
                    + "<a href=\"" + link + "\">이메일 인증</a>"
                    + "<p>이 링크는 본인 인증을 위한 것으로 최초 1회만 사용될 것입니다.</p>"
                    + "<p>인증 완료 후 로그인 시도를 한 번 해보시고, 만약 로그인이 되지 않을 시 총학생회에 문의해주십시오.</p>"
                    + "<p>또한 앞으로 로그인 시 이 이메일로 인증번호 6자리가 발송될 것입니다."
                    + "<p>순리미에 오신 걸 환영합니다!</p>";


            helper.setText(htmlContent, true);

            mailSender.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException("메일 발송 실패", e);
        }
    }

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
        admin.setAffiliation(req.getAffiliation());
        admin.setEmailVerified(false);
        String token = UUID.randomUUID().toString();
        emailService.saveToken(req.getUserId(), token);
        admin.setEmailVerificationToken(token);

        List<Department> departments = departmentRepository.findAllById(req.getDepartmentIds());
        admin.setDepartments(new HashSet<>(departments));

        Admin saved = adminRepository.save(admin);
        sendVerificationMail(saved.getUserId());

        emailService.removeToken(req.getUserId());

        return new AdminDto.SignupResponse(saved.getId(), saved.getUserId(), saved.getName(), saved.getAffiliation());
    }

    public AdminDto.MessageResponse resetPassword(AdminDto.ResetPasswordRequest req) {
        Admin admin = adminRepository.findByUserId(req.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 아이디가 존재하지 않습니다."));

        // 암호화 저장
        admin.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        adminRepository.save(admin);

        // 임시 비밀번호 반환
        return new AdminDto.MessageResponse("비밀번호가 수정 되었습니다.");
    }

    @Transactional
    public void markEmailAsVerified(String userId) {
        Admin admin = adminRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다."));
        admin.setEmailVerified(true);
        adminRepository.save(admin);
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

    public List<InternalNoticeDto.InternalNoticeListResponse> getMyInternalNotice(Authentication jwtToken) {
        String userIdInToken=jwtToken.getName();
        Admin getCurrentAdmin = adminRepository.findByUserId(userIdInToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다."));

        List<InternalNotice> notices = internalNoticeRepository.findByWriter(getCurrentAdmin);
        return notices.stream().map(InternalNoticeDto.InternalNoticeListResponse::new).toList();
    }

    public List<Department> getAllDepartment() {
        return departmentRepository.findAll().stream().toList();
    }

    public List<AdminDto.AdminUserResponse> getAllAdmins() {
        return adminRepository.findAll()
                .stream()
                .map(AdminDto.AdminUserResponse::new)
                .toList();
    }

    public AdminDto.AdminUserResponse updateAdmin(Long adminId, AdminDto.AdminUpdateRequest req) {
        if (!adminRegisterPassword.equals(req.getRegisterPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "보안 비밀번호가 일치하지 않습니다.");
        }

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다."));

        if (req.getName() != null && !req.getName().isBlank()) {
            admin.setName(req.getName());
        }
        if (req.getAffiliation() != null) {
            admin.setAffiliation(req.getAffiliation());
        }
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            admin.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        }
        // departmentIds 처리 (null이 아닌 경우 무조건 set, 빈 배열이면 기존 값 삭제)
        if (req.getDepartmentIds() != null) {
            List<Department> foundDepartments = departmentRepository.findAllById(req.getDepartmentIds());
            if (foundDepartments.size() != req.getDepartmentIds().size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 학과 ID가 포함되어 있습니다.");
            }
            admin.setDepartments(new HashSet<>(foundDepartments));
        }
        // categories 처리 (null이 아닌 경우 무조건 set)
        if (req.getCategories() != null) {
            Set<com.schnofiticationbe.entity.Category> categories = new HashSet<>();
            for (String cat : req.getCategories()) {
                try {
                    categories.add(com.schnofiticationbe.entity.Category.valueOf(cat));
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리: " + cat);
                }
            }
            admin.setCategories(categories);
        }
        // grades 처리 (null이 아닌 경우 무조건 set)
        if (req.getGrades() != null) {
            Set<com.schnofiticationbe.entity.TargetYear> grades = new HashSet<>();
            for (String grade : req.getGrades()) {
                try {
                    grades.add(com.schnofiticationbe.entity.TargetYear.valueOf(grade));
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 학년: " + grade);
                }
            }
            admin.setGrades(grades);
        }

        Admin saved = adminRepository.save(admin);
        return new AdminDto.AdminUserResponse(saved);
    }

    public AdminDto.MessageResponse deleteAdmin(Long adminId, AdminDto.AdminDeleteRequest req) {
        if (!adminRegisterPassword.equals(req.getRegisterPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "보안 비밀번호가 일치하지 않습니다.");
        }

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다."));

        adminRepository.delete(admin);
        return new AdminDto.MessageResponse("관리자 계정이 삭제되었습니다.");
    }
}
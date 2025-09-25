package com.schnofiticationbe.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendMail(String to, String subject, String text) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setFrom("soonrimi.noti@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, false);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("메일 발송 실패", e);
        }
    }

    public void sendOtp(String to, int otp) {
        String subject = "[순천향대학교 Soonrimi] 로그인 OTP 코드";
        String text = "OTP 코드는 " + otp + " 입니다. 5분 안에 입력해주시기 바랍니다.";
        sendMail(to, subject, text);
    }

    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();
    private final Set<String> verifiedEmails = ConcurrentHashMap.newKeySet();

    public void saveToken(String email, String token) {
        tokenStore.put(email, token);
    }

    public boolean verifyToken(String email, String token) {
        return token.equals(tokenStore.get(email));
    }

    public void removeToken(String email) {
        tokenStore.remove(email);
    }

    public void markVerified(String email) {
        verifiedEmails.add(email);
    }

    public boolean isVerified(String email) {
        return verifiedEmails.contains(email);
    }
}

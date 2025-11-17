package com.schnofiticationbe.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
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

    @Async
    public void sendOtp(String toEmail, int otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("[Soonrimi] 로그인 OTP 코드");
            helper.setText(
                    "요청하신 OTP 코드는 다음과 같습니다.\n\n" +
                            otp + "\n\n" +
                            "5분 안에 입력해 주세요.",
                    false
            );

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();

    public void removeToken(String email) {
        tokenStore.remove(email);
    }
}

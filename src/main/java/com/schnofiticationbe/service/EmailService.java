package com.schnofiticationbe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendOtp(String to, int otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("soonrimi.noti@gmail.com"); // 발신자 (고정)
        msg.setTo(to);                                     // 수신자 (관리자 이메일)
        msg.setSubject("[순천향대학교 Soonrimi] 로그인 OTP 코드");
        msg.setText("OTP 코드는 " + otp + " 입니다. 5분 안에 입력해주시기 바랍니다.");
        mailSender.send(msg);
    }
}

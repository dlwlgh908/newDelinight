package com.onetouch.delinight.Util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();

        String from = "운영자<linkon9277@gmail.com>";
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        try {
            javaMailSender.send(message);
        }catch (Exception e) {
            System.out.println("자바메일센더 서비스 메일전송 실패");
            e.printStackTrace();
        }
        System.out.println("메일 전송 완료");
    }

}

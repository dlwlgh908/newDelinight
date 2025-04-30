/*****************************
 * 작성자 : 이동건
 * 공용모듈
 * 필요할 때마다 사용가능한 메소드
 * ***************************/
package com.onetouch.delinight.Util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    // 초기 셋팅
    public void sendHtmlEmail(String to, String subject, String templateName, String tempPassword, Map<String, Object> variables) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            // 타임리프 context에 변수 추가 (DTO 대신 Map 사용)
            Context context = new Context();
            context.setVariables(variables); // Map 데이터 추가
            context.setVariable("message", tempPassword);

            // 템플릿 엔진을 사용하여 HTML 렌더링
            String htmlTemplate = templateEngine.process(templateName, context);

            // 이메일 설정
            helper.setFrom("linkon9277@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlTemplate, true);


            // 이메일 발송
            javaMailSender.send(message);

        }catch (MessagingException e){
            throw new RuntimeException("이메일 전송 중 오류 발생");
        }

    }

    @Async
    public CompletableFuture<Object> sendNpsEmail(String email, String name, String surveyLink, Long checkOutId){

        try{

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("email", email);
            context.setVariable("name", name);
            context.setVariable("surveyLink", surveyLink);
            context.setVariable("checkOutId", checkOutId);

            String html = templateEngine.process("users/nps/npsmail", context);

            helper.setFrom("linkon9277@gmail.com");
            helper.setTo(email);
            helper.setSubject("NPS 설문 참여 요청");
            helper.setText(html, true);

            javaMailSender.send(message);
            return CompletableFuture.completedFuture(null);
        }catch (MessagingException e) {
            log.error("이메일 전송 중 오류 발생 - email: {}, name: {}, link: {}", email, name, surveyLink, e);
            throw new RuntimeException("이메일 전송 중 오류 발생", e); // 원래 예외를 함께 던짐
        }

    }


}

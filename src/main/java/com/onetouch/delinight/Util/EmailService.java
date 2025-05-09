/*****************************
 * 작성자 : 이동건
 * 공용모듈
 * 필요할 때마다 사용가능한 메소드
 * ***************************/
package com.onetouch.delinight.Util;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.PerformanceMailDTO;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.OpenAIService;
import com.onetouch.delinight.Service.PaymentService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final PaymentService paymentService;
    private final MembersService membersService;
    private final OpenAIService  openAIService;

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
            throw new RuntimeException("이메일 전송 중 오류 발생", e);
        }

    }


    @Transactional
    public void sendDailyPerformance(PerformanceMailDTO mailDTO){
        MembersDTO membersDTO = membersService.findByEmail(mailDTO.getEmail());
        if(!membersService.checkOperation(membersDTO)){
            log.info("빠져나옴");

            return;
        }

        try{

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());


            Context context = new Context();
            context.setVariable("email", mailDTO.getEmail());
            context.setVariable("name", mailDTO.getName());
            context.setVariable("date", mailDTO.getDate());
            context.setVariable("aiResponse",mailDTO.getAiResponse());
            context.setVariable("targetName",mailDTO.getTargetName());
            helper.addAttachment(mailDTO.getDate().toString()+"_일일매출현황.xlsx",new ByteArrayResource(paymentService.extractDailyExcel(membersDTO.getId(), membersDTO.getRole())));
            context.setVariable("aiResponse", openAIService.analyzeSales(paymentService.makePrompt(membersDTO.getId(), membersDTO.getRole())));

            log.info(mailDTO);

            String html = templateEngine.process("/members/payment/dailyPerformance", context);

            helper.setFrom("linkon9277@gmail.com");
            helper.setTo(mailDTO.getEmail());
            helper.setSubject(mailDTO.getTargetName()+"일일 매출 현황");
            helper.setText(html, true);

            javaMailSender.send(message);
        }catch (MessagingException e) {
            log.error("이메일 전송 중 오류 발생 - email: {}, name: {}", mailDTO.getEmail(), mailDTO.getName(), e);
            throw new RuntimeException("이메일 전송 중 오류 발생", e); // 원래 예외를 함께 던짐
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}

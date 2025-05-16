/*****************************
 * 작성자 : 이동건
 * 공용모듈
 * 필요할 때마다 사용가능한 메소드
 * ***************************/
package com.onetouch.delinight.Util;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.PerformanceMailDTO;
import com.onetouch.delinight.Repository.PaymentRepository;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.NetPromoterScoreService;
import com.onetouch.delinight.Service.OpenAIService;
import com.onetouch.delinight.Service.PaymentService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailService {
    private final PaymentRepository paymentRepository;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final PaymentService paymentService;
    private final MembersService membersService;
    private final OpenAIService  openAIService;
    private final NetPromoterScoreService netPromoterScoreService;

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

        try{

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());



            Context context = new Context();

            context.setVariable("email", mailDTO.getEmail());
            context.setVariable("name", mailDTO.getName());
            context.setVariable("date", mailDTO.getDate());
            context.setVariable("targetName",mailDTO.getTargetName());
            String totalPrice = paymentService.dailyPerformancePrice(mailDTO.getEmail());
            context.setVariable("totalPrice",totalPrice);
            if(netPromoterScoreService.npsOperationCheck(mailDTO.getEmail())) {
                List<Integer> npsScore = netPromoterScoreService.dailyPerformanceScore(mailDTO.getEmail());
                context.setVariable("totalScore", npsScore.get(0));
                context.setVariable("firstScore", npsScore.get(1));
                context.setVariable("secondScore", npsScore.get(2));
                context.setVariable("thirdScore", npsScore.get(3));
                context.setVariable("fourthScore", npsScore.get(4));
                context.setVariable("fifthScore", npsScore.get(5));
            }
            log.info(mailDTO);
            helper.addAttachment(mailDTO.getDate().toString()+"_일일매출현황.xlsx",new ByteArrayResource(paymentService.extractDailyExcel(membersDTO.getId(), membersDTO.getRole())));
            context.setVariable("aiResponse", openAIService.analyzeSales(openAIService.makePrompt(membersDTO.getId(), membersDTO.getRole(), mailDTO.getTargetName())));


            String html = templateEngine.process("/members/payment/dailyPerformance", context);

            helper.setFrom("linkon9277@gmail.com");
            helper.setTo("dlwlgh908@naver.com");
            helper.setSubject(mailDTO.getTargetName()+"일일 매출 현황");
            helper.setText(html, true);

            FileSystemResource result = new FileSystemResource(new File("src/main/resources/static/img/AI.png"));
            helper.addInline("aiImage", result);


            javaMailSender.send(message);
        }catch (MessagingException e) {
            log.error("이메일 전송 중 오류 발생 - email: {}, name: {}", mailDTO.getEmail(), mailDTO.getName(), e);
            throw new RuntimeException("이메일 전송 중 오류 발생", e); // 원래 예외를 함께 던짐
        } catch (IOException e) {
            log.info(e.getMessage()+mailDTO);
            throw new RuntimeException(e);
        }

    }


}

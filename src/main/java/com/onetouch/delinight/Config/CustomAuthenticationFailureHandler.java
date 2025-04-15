package com.onetouch.delinight.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("로그인 실패 원인 : " + exception.getMessage()+exception.getLocalizedMessage());

        log.info("실제 예외 클래스: " + exception.getClass());
        log.info("실제 예외 클래스: " + exception.getClass());

        String errorParam = "unknown";

        if (exception instanceof UsernameNotFoundException) {
            log.info("아이디 if문 돌아가기시작");
            log.info("아이디 if문 돌아가기시작");
            log.info("아이디 if문 돌아가기시작");
            errorParam = "user_not_found";
            log.info("아이디가 존재하지 않음!!!!!");
            log.info("아이디가 존재하지 않음!!!!!");
            log.info("아이디가 존재하지 않음!!!!!");
        }else if (exception instanceof InternalAuthenticationServiceException) {
            Throwable cause = exception.getCause();
            if (cause instanceof UsernameNotFoundException) {
                log.info("UsernameNotFoundException 안에 숨어 있었음!");
                log.info("UsernameNotFoundException 안에 숨어 있었음!");
                log.info("UsernameNotFoundException 안에 숨어 있었음!");
                errorParam = "user_not_found";
            }
        } else if (exception instanceof BadCredentialsException) {
            log.info("비번 if문 돌아가기 시작");
            log.info("비번 if문 돌아가기 시작");
            log.info("비번 if문 돌아가기 시작");
            errorParam = "bad_credentials";
            log.info("아이디는 맞는데 비번이 틀림!!");
            log.info("아이디는 맞는데 비번이 틀림!!");
            log.info("아이디는 맞는데 비번이 틀림!!");
        }

        response.sendRedirect("/members/account/adminlogin?error=" + errorParam);
    }

}

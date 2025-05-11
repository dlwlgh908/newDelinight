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

        String errorParam = "unknown";

        if (exception instanceof UsernameNotFoundException) {
            errorParam = "user_not_found";
        }else if (exception instanceof InternalAuthenticationServiceException) {

            Throwable cause = exception.getCause();
            log.info("로그인 실패 - 원인: {}", exception.getMessage());
            if (cause instanceof UsernameNotFoundException) {
                errorParam = "user_not_found";
                log.info("로그인 실패 - 원인: {}", exception.getMessage());
            }
        } else if (exception instanceof BadCredentialsException) {
            errorParam = "bad_credentials";
            log.info("로그인 실패 - 원인: {}", exception.getMessage());
            log.info("아이디는 맞는데 비번이 틀림!!");
        }

        response.sendRedirect("/members/account/login?error=" + errorParam);
    }

//이이디 할 때는 왜 안될까.
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        log.info("로그인 실패 원인 : " + exception.getMessage());
//
//        String errorParam = "";
//        String loginPage = "/members/account/login"; // 기본값 (member 로그인 페이지)
//
//        if (exception instanceof UsernameNotFoundException) {
//            errorParam = "users_not_found";
//        } else if (exception instanceof BadCredentialsException) {
//            errorParam = "bad_credentials";
//        }
//
//        // 🔹 사용자가 어떤 로그인 페이지에서 로그인했는지 확인
//        String requestURI = request.getRequestURI();
//
//        if (requestURI.contains("/users/login")) {
//            loginPage = "/users/login";
//        } else if (requestURI.contains("/members/account/login")) {
//            loginPage = "/members/account/login";
//        } else if (requestURI.contains("/guests/login")) {
//            loginPage = "/guests/login";
//        }
//
//        response.sendRedirect(loginPage + "?error=" + errorParam);
//    }




}

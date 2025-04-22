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
            if (cause instanceof UsernameNotFoundException) {
                errorParam = "user_not_found";
            }
        } else if (exception instanceof BadCredentialsException) {
            errorParam = "bad_credentials";
            log.info("아이디는 맞는데 비번이 틀림!!");
        }

        response.sendRedirect("/members/account/login?error=" + errorParam);
    }

}

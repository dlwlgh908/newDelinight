package com.onetouch.delinight.Controller.UtilController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException ex, HttpServletResponse response) throws IOException{
        log.error("Exception 발생 : ", ex);
        response.sendRedirect("/members/account/login");
    }


}

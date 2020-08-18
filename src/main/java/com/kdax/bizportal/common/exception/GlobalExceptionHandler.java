package com.kdax.bizportal.common.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;

    @Autowired
    MessageSource messageSource;

    public boolean isApiCall(){
        if(request.getRequestURI().endsWith(".do")){
            return false;
        }
        return true;
    }

    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BizExceptionMessage.class)
    public ServerErrorResponse BizExceptionMessageHandler(BizExceptionMessage ge) throws IOException {
        if(!ge.getMsgTypCod().equals("Q")){
            ge.printStackTrace();
            log.warn("## BizExceptionHandler: ({}) {}",
                    ge.getCode(),
                    ge.getMessage());
        }

        if(!isApiCall()){
            response.sendRedirect(request.getContextPath() + "/bizPortalError.html");
            return null;
        }
        else{

            //return new ServerErrorResponse(ge.getMsgTypCod(),  ge.getErrorType(), ge.getMessage() );
            return new ServerErrorResponse(ge.getMsgTypCod(),  ge.getErrorType(),
                    messageSource.getMessage( ge.getErrorType().getMessageKey(), null, ge.getMsgLocale())  );
        }
    }
}

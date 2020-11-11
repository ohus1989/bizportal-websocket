package com.kdax.bizportal.common.exception;


import com.google.gson.Gson;
import com.kdax.bizportal.common.voCommon.LogComVO;
import com.kdax.bizportal.common.voCommon.LogReqVO;
import com.kdax.bizportal.common.voCommon.LogResVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

            LogReqVO logReqVO = new LogReqVO();
            LogResVO logResVO = new LogResVO();
            LogComVO logComVO = new LogComVO();

            try {
                logReqVO.setReqUri(request.getRequestURI());
                logReqVO.setParameters(request.getParameterMap());

                Map headerMap = new HashMap();
                for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements(); ) {
                    String nextElement = e.nextElement();
                    headerMap.put(nextElement, request.getHeader(nextElement));
                }

                logReqVO.setHeaders(headerMap);

                if (headerMap.get("Content-Type") != null && headerMap.get("Content-Type").toString().contains(APPLICATION_JSON_VALUE)) {
                    logReqVO.setBody(IOUtils.toString(request.getReader()));
                }

                logReqVO.setContentType(request.getContentType());

            } catch (Exception e) {
                log.error("logging LogReqVO error::{}", e);
            } finally {
                logResVO.setCode(ge.getCode());
                logResVO.setMessage(ge.getMessage());

                StackTraceElement traceElement = ge.getStackTrace()[0];
                logComVO.setTraceElement(traceElement);

//                logComVO.setBizExceptionMessage(ge);
            }
            logComVO.setRequest(logReqVO);
            logComVO.setResponse(logResVO);

            //ge.printStackTrace();
            log.warn("BizExceptionMessage trace ",ge);
            log.error("## BizExceptionHandler:{}",new Gson().toJson(logComVO));
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

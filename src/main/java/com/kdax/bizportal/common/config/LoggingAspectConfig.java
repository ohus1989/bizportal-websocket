package com.kdax.bizportal.common.config;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.kdax.bizportal.common.exception.BizExceptionMessage;
import com.kdax.bizportal.common.exception.ServerErrorResponse;
import com.kdax.bizportal.common.session.VerifyToken;
import com.kdax.bizportal.common.voCommon.LogComVO;
import com.kdax.bizportal.common.voCommon.LogReqVO;
import com.kdax.bizportal.common.voCommon.LogResVO;
import com.kdax.bizportal.common.voHeader.ActionResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Aspect
@Slf4j
public class LoggingAspectConfig {

    @Autowired
    Environment env;

    @Autowired
    MessageSource messageSource;

    @Autowired
    VerifyToken verifyToken;

    Gson gson = new Gson();

    @Autowired
    private HttpServletResponse response;

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }

//    @Pointcut("within(com.kdax.bizportal.auth.modules...*)") // 3
//    public void onRequest() {
//
//        log.debug("LoggingAspectConfig!!!!!!!!! onRequest");
//    }

//    @Before("execution(* com.kdax.bizportal..**Api.*(..))")
//    public void startLog(JoinPoint jp) {
//
//        log.info("-------------------------------------");
//        log.info("-------------------------------------");
//
//        /* ???????????? ?????? ?????????????????? Object??? ????????? ????????????. */
//        log.info("1:" + new Gson().toJson(jp.getArgs()));
//
//        /* ?????? Advice??? ????????? ????????????. */
//        log.info("2:" + jp.getKind());
//
//        /* ???????????? ?????? ????????? ???????????? ?????? ????????? ????????? ??? ?????? */
//        log.info("3:" + jp.getSignature().getName());
//
//        /* target ????????? ????????? ??? ?????? */
//        log.info("4:" + jp.getTarget().toString());
//
//        /* Advice??? ????????? ????????? ????????? ??? ?????? */
//        log.info("5:" + jp.getThis().toString());
//
//    }

    //    @Around("execution(* com.kdax.bizportal.basicbiz.modules.coinevent.CoinEventApi.selectCoinEventList(..))")
    // 4
    @Around("execution(* com.kdax.bizportal..*Api.*(..))")
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
        String lockENV = env.getProperty("spring.profiles.active");
//        MDC.put("txid", UUID.randomUUID().toString());
        String txid = String.format("%s%s", lockENV.toUpperCase().substring(0, 3), LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        MDC.put("txid", txid);

        LogReqVO logReqVO = new LogReqVO();

        try {

            logReqVO.setTxid(txid);
            logReqVO.setClassName(pjp.getSignature().getDeclaringTypeName());
            logReqVO.setClassMethod(pjp.getSignature().getName());

            HttpServletRequest request = // 5
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();


            logReqVO.setReqUri(request.getRequestURI());
            logReqVO.setParameters(request.getParameterMap());
            logReqVO.setArgs(pjp.getArgs());

            Map headerMap = new HashMap();
            for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements(); ) {
                String nextElement = e.nextElement();
                headerMap.put(nextElement, request.getHeader(nextElement));
            }

            logReqVO.setHeaders(headerMap);
            logReqVO.setContentType(request.getContentType());

            if (headerMap.get("Content-Type") != null && headerMap.get("Content-Type").toString().contains(APPLICATION_JSON_VALUE)) {
                logReqVO.setBody(IOUtils.toString(request.getReader()));
            }

        } catch (Exception e) {
            log.debug("logging LogReqVO error::{}", e);
        }

//        log.info("Logging START logReqVO::{}", gson.toJson(logReqVO));

        long start = System.currentTimeMillis();
        Object reobj = null;
        try {
            reobj = pjp.proceed(pjp.getArgs());// 6
            return reobj;
        } catch (BizExceptionMessage ex) {
            reobj = new ServerErrorResponse(ex);
//            reobj = new ServerErrorResponse(ex.getMsgTypCod(),  ex.getErrorType(),messageSource.getMessage( ex.getErrorType().getMessageKey(), null, ex.getMsgLocale())  );
            throw ex;
        } catch (Exception e) {

            Signature sig = pjp.getSignature();
            Object[] args = pjp.getArgs();

            String location = sig.getDeclaringTypeName() + '.' + sig.getName() + ", args=" + Arrays.toString(args);
            log.debug("exception within " + location, e);
            throw e;
        } finally {
            try {
                Map resHeaders = response.getHeaderNames().stream().collect(Collectors.toMap(Function.identity(),
                        h -> new ArrayList<>(response.getHeaders(h)), (oldValue, newValue) -> newValue, HttpHeaders::new));

                long end = System.currentTimeMillis();
                LogResVO logResVO = new LogResVO();
                logResVO.setTxid(txid);
                long contentsLength = gson.toJson(reobj).length();
                if (contentsLength > 1000) {
                    Map item = new HashMap();
                    item.put("contentsLength", contentsLength);
                    if (reobj instanceof ActionResultVO &&
                            ((ActionResultVO) reobj).getResultVO() instanceof List) {
                        List reslist = ((List) ((ActionResultVO) reobj).getResultVO());
                        int listSize = reslist.size();
                        item.put("listSize", listSize);
                        item.put("list0", reslist.get(0));
                    }
                    logResVO.setBody(item);
                } else {
                    logResVO.setBody(reobj);
                }

                logResVO.setStatus(response.getStatus());
                logResVO.setContentType(response.getContentType());
                logResVO.setHeaders(resHeaders);
                logResVO.setResponseTime(end - start);

                LogComVO logComVO = new LogComVO();
                logComVO.setRequest(logReqVO);
                logComVO.setResponse(logResVO);

                logComVO.setAuthTokenVO(verifyToken.getAuthTokenFromTokenHeader());

//        log.info("Logging END logResVO::{}", gson.toJson(logResVO));
                log.info("Logging END logComVO::{}", gson.toJson(logComVO));
            } catch (Exception e) {
                log.debug("logging logResVO error::{}", e);
            }

        }

    }

}
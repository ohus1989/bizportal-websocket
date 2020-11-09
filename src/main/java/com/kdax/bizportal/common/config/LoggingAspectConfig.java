package com.kdax.bizportal.common.config;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.kdax.bizportal.common.exception.BizExceptionMessage;
import com.kdax.bizportal.common.exception.GlobalExceptionHandler;
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

@Component // 1
@Aspect // 2
@Slf4j
public class LoggingAspectConfig {

    @Autowired
    Environment env;

    @Autowired
    GlobalExceptionHandler globalExceptionHandler;

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
//        /* 전달되는 모든 파라미터들을 Object의 배열로 가져온다. */
//        log.info("1:" + new Gson().toJson(jp.getArgs()));
//
//        /* 해당 Advice의 타입을 알아낸다. */
//        log.info("2:" + jp.getKind());
//
//        /* 실행하는 대상 객체의 메소드에 대한 정보를 알아낼 때 사용 */
//        log.info("3:" + jp.getSignature().getName());
//
//        /* target 객체를 알아낼 때 사용 */
//        log.info("4:" + jp.getTarget().toString());
//
//        /* Advice를 행하는 객체를 알아낼 때 사용 */
//        log.info("5:" + jp.getThis().toString());
//
//    }

    //    @Around("execution(* com.kdax.bizportal.basicbiz.modules.coinevent.CoinEventApi.selectCoinEventList(..))")
    // 4
    @Around("execution(* com.kdax.bizportal..**Api.*(..))")
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


            Map<String, String[]> paramMap = request.getParameterMap();
            String params = "";
            if (paramMap.isEmpty() == false) {
                params = " [" + paramMapToString(paramMap) + "]";
            }

        } catch (Exception e) {
            log.error("logging LogReqVO error::{}", e);
        }

//        log.info("Logging START logReqVO::{}", gson.toJson(logReqVO));

        long start = System.currentTimeMillis();
        Object reobj = null;
        try {
            reobj = pjp.proceed(pjp.getArgs());// 6
            return reobj;
        } catch (BizExceptionMessage ex) {
            reobj = globalExceptionHandler.BizExceptionMessageHandler(ex);
            throw ex;
        } catch (Exception e) {

            Signature sig = pjp.getSignature();
            Object[] args = pjp.getArgs();

            String location = sig.getDeclaringTypeName() + '.' + sig.getName() + ", args=" + Arrays.toString(args);
            log.error("exception within " + location, e);
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

//        log.info("Logging END logResVO::{}", gson.toJson(logResVO));
                log.info("Logging END logComVO::{}", gson.toJson(logComVO));
            } catch (Exception e) {
                log.error("logging logResVO error::{}", e);
            }

        }

    }

}
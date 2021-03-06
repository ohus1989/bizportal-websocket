package com.kdax.bizportal.common.session;

import com.google.gson.Gson;
import com.kdax.bizportal.common.annotation.AcessScope;
import com.kdax.bizportal.common.constants.GlobalConstants;
import com.kdax.bizportal.common.enums.AccessScopeType;
import com.kdax.bizportal.common.exception.BizExceptionMessage;
import com.kdax.bizportal.common.exception.ErrorType;
import com.kdax.bizportal.common.util.SeedCipher;
import com.kdax.bizportal.common.voCommon.UserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Component
@Slf4j
@Aspect
public class VerifyToken {
    @Value("${bizportal.domain.code}")
    private String bizportalDomainCode;

    @Value("${bizportal.domain.smkey}")
    private String bizportalDomainSmKey;

    @Value("${bizportal.auth.token.android.expire.minute}")
    private int androidExpireMinute;

    @Value("${bizportal.auth.token.web.expire.minute}")
    private int webExpireMinute;

    @Value("${bizportal.auth.default.flag}")
    private boolean authDefaultFlag;

    @Autowired
    HttpServletRequest request;

    @Autowired
    Environment env;

    @Autowired
    RedisManager redisManager;

    public String makeToken(AuthTokenVO userInfo) {
        String jwtString = Jwts
                .builder()
                .setHeaderParam("tpy", "JWT")
                .claim("id", "haha")
                .signWith(SignatureAlgorithm.HS256, GlobalConstants.JWT_USER_KEY)
                .compact();
        return jwtString;
    }

    // check parsing error
    public boolean isUseableToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts
                    .parser()
                    .setSigningKey(GlobalConstants.JWT_USER_KEY)
                    .parseClaimsJws(token);
            //logger.debug("token : {}", claimsJws);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Jws<Claims> getDecodeToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(GlobalConstants.JWT_USER_KEY)
                .parseClaimsJws(token);
    }

    @Around("@annotation(com.kdax.bizportal.common.annotation.AcessScope)")
    public Object doSomethingAround(final ProceedingJoinPoint joinPoint) throws Throwable {

        String token = request.getHeader(GlobalConstants.TOKEN_HEADER);
        boolean flag = authDefaultFlag; //false default , for test

        if (!StringUtils.isEmpty(token)) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            AcessScope acessScope = method.getAnnotation(AcessScope.class);

            //?????? ?????? , ?????? redis ??????
            //sessionManager.setSession(authTokenVO);

            if ((acessScope.scope() == AccessScopeType.PRIVATE ||
                    acessScope.scope() == AccessScopeType.SYSTEM ||
                    acessScope.scope() == AccessScopeType.WITHOUT_OTP_PRIVATE)) {
                AuthTokenVO authTokenVO = getAuthTokenFromString(token);
//                otp ?????? ????????? ??????
                if ((authTokenVO.getIsOtp() == null || !authTokenVO.getIsOtp()) && acessScope.scope() != AccessScopeType.WITHOUT_OTP_PRIVATE) {
                    throw new BizExceptionMessage(ErrorType.NOT_OTP_REGISTRANT);
                } else {
                    flag = true;
                }
            } else if (acessScope.scope() == AccessScopeType.PUBLIC) {
                flag = true;
            }

        } else {
            // ???????????? ?????? ??????????????? ??????
            //sessionManager.setTestDefault();
        }

        if (flag) {
            /*
            Object[] args = Arrays.stream(joinPoint.getArgs()).map(data ->
                        { if(data instanceof String) { data = pcheck; } return data; }).toArray();
             */

            return joinPoint.proceed();
        } else {
            log.error("access deny api.. ");
            throw new BizExceptionMessage(ErrorType.NOT_AUTH);
        }
    }

    public AuthTokenVO getAuthTokenFromString(String token) {
        AuthTokenVO authTokenVO = null;

        try {
            if (StringUtils.isEmpty(token)) {
                log.warn("invaild token {}", token);
                throw new BizExceptionMessage(ErrorType.NOT_INVALID_TOKEN);
            }

            Jws<Claims> jwsClaims = getDecodeToken(token);
            Object jwsClaimsString = jwsClaims
                    .getBody()
                    .get(GlobalConstants.TOKEN_JWTID);

            Gson gson = new Gson();
            authTokenVO = gson.fromJson((String) jwsClaimsString, AuthTokenVO.class);

            if (authTokenVO == null) {
                log.warn("Token data is invaliad! : {}", token);
                throw new BizExceptionMessage(ErrorType.NOT_INVALID_TOKEN);
            }

            //inputTokenVO.getUgpGrpCods() added, integrity check
            SeedCipher sc = new SeedCipher();

            String lockENV = env.getProperty("spring.profiles.active");
            if (!("local".equals(lockENV) || "testdb".equals(lockENV))) {
                if (!authTokenVO
                        .getUuid()
                        .equals(sc.SHA256(getRemoteIp() + authTokenVO.getUserLevel()))) {
                    log.warn("Token uui is invaliad! : {}", token);
                    throw new BizExceptionMessage(ErrorType.NOT_INVALID_TOKEN);
                }
            }

            Calendar today = Calendar.getInstance();
            today.setTime(new Date());

            if (authTokenVO
                    .getExpireDate()
                    .compareTo(today.getTime()) == -1) {
                log.warn("token is expired {}", authTokenVO.getExpireDate());
                throw new BizExceptionMessage(ErrorType.NOT_INVALID_TOKEN);
            }

        } catch (Exception e) {
            log.warn("Exception : {}", e.getMessage());
            throw new BizExceptionMessage(ErrorType.NOT_INVALID_TOKEN);
        }
        return authTokenVO;
    }

    public String getToken(UserVO userInfoVO) {

        AuthTokenVO authTokenVO = new AuthTokenVO();
        String jwtString = "";
        Gson gson = new Gson();

        try {
            SeedCipher sc = new SeedCipher();

            //TUser-Agent ???????????? ?????? ??????
            String userAgent = request.getHeader("User-Agent");
            log.info("User-Agent:\n{}", userAgent);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            if (userAgent != null && userAgent.contains("Android")) {
                cal.add(Calendar.MINUTE, androidExpireMinute);
            } else {
                cal.add(Calendar.MINUTE, webExpireMinute);
            }

            authTokenVO.setUserCodeId(userInfoVO.getCodeId());

            String lockENV = env.getProperty("spring.profiles.active");
            if (lockENV != null && "testdb|dev".contains(lockENV)) {
                authTokenVO.setIsOtp(true);
            } else {
                UserRedisInfoVO userSessionInfo = redisManager.getUserSessionInfo(userInfoVO.getCodeId());
                if (userSessionInfo != null) {
                    authTokenVO.setIsOtp(userSessionInfo.getIsOtp());
                }
            }

            authTokenVO.setUserLevel("DEV");  // ?????? ?????? ?????? ??????
            authTokenVO.setExpireDate(cal.getTime());
            authTokenVO.setUserLocale(new Locale(userInfoVO.getServiceLocale()));
            authTokenVO.setUuid(sc.SHA256(getRemoteIp() + authTokenVO.getUserLevel()));

            String authToken = gson.toJson(authTokenVO);
            log.info("authToken :: {}", authToken);
            //log.info("descrypt token : {}", sc.decryptAsString(sc.getByteFromHexString(authToken), cmk.getBytes
            // (GlobalConstants.DEFAULT_CHARSET)));
            jwtString = Jwts
                    .builder()
                    .setHeaderParam("tpy", "JWT")
                    .claim(GlobalConstants.TOKEN_JWTID, authToken)
                    .signWith(SignatureAlgorithm.HS256, GlobalConstants.JWT_USER_KEY)
                    .compact();

        } catch (Exception e) {
            log.warn("Exception : {}", e.getMessage());
            throw new BizExceptionMessage(ErrorType.NOT_FOUND_USER_ERROR, userInfoVO.getServiceLocale());
        }

        return jwtString;
    }

    public String getRemoteIp() {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getUserCodeId() {
        try {
            AuthTokenVO token = this.getAuthTokenFromTokenHeader();
            if (token != null) {
                return token.getUserCodeId();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public AuthTokenVO getAuthTokenFromTokenHeader() {
        String token = request.getHeader(GlobalConstants.TOKEN_HEADER);
        if (token != null) {
            return this.getAuthTokenFromString(token);
        } else {
            return null;
        }
    }
}

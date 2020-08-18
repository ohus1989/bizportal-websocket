package com.kdax.bizportal.common.session;

import com.kdax.bizportal.common.annotation.AcessScope;
import com.kdax.bizportal.common.constants.GlobalConstants;
import com.kdax.bizportal.common.enums.AccessScopeType;
import com.kdax.bizportal.common.exception.BizExceptionMessage;
import com.kdax.bizportal.common.exception.ErrorType;
import com.kdax.bizportal.common.util.SeedCipher;
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
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
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

    @Value("${bizportal.auth.token.expire.hour}")
    private int expireHour;

    @Autowired
    HttpServletRequest request;

    public String makeToken(AuthTokenVO userInfo){
        String jwtString = Jwts.builder()
                .setHeaderParam("tpy","JWT")
                .claim("id", "haha")
                .signWith(SignatureAlgorithm.HS256, GlobalConstants.JWT_USER_KEY)
                .compact();
        return jwtString;
    }
    // check parsing error
    public boolean isUseableToken(String token){
        try{
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(GlobalConstants.JWT_USER_KEY)
                    .parseClaimsJws(token);
            //logger.debug("token : {}", claimsJws);
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    public Jws<Claims> getDecodeToken(String token){
        return Jwts.parser()
                .setSigningKey(GlobalConstants.JWT_USER_KEY)
                .parseClaimsJws(token);
    }

    @Around("@annotation(com.kdax.bizportal.common.annotation.AcessScope)")
    public Object doSomethingAround(final ProceedingJoinPoint joinPoint) throws Throwable {

        String token = request.getHeader(GlobalConstants.TOKEN_HEADER);
        boolean flag = true; //false default , for test

        if(!StringUtils.isEmpty(token)) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            AcessScope acessScope = method.getAnnotation(AcessScope.class);

            //세션 셋팅 , 향후 redis 고려
            //sessionManager.setSession(authTokenVO);

            if( (acessScope.scope() == AccessScopeType.PRIVATE ||
                    acessScope.scope() == AccessScopeType.SYSTEM) ){
                AuthTokenVO authTokenVO = getAuthTokenFromString(token);
                flag = true;
            }
            else if(acessScope.scope() == AccessScopeType.PUBLIC){
                flag = true;
            }
        }else {
            // 이부분은 초기 개발시에만 사용
            //sessionManager.setTestDefault();
        }

        if(flag) {
            /*
            Object[] args = Arrays.stream(joinPoint.getArgs()).map(data ->
                        { if(data instanceof String) { data = pcheck; } return data; }).toArray();
             */

            return joinPoint.proceed();
        }
        else{
            log.error("access deny api.. ");
            throw new BizExceptionMessage(ErrorType.NOT_AUTH);
        }
    }

    public AuthTokenVO getAuthTokenFromString(String token){
        AuthTokenVO authTokenVO = null;
        try {
            if(StringUtils.isEmpty(token) ){
                log.warn("invaild token {}", token);
                throw new BizExceptionMessage(ErrorType.NOT_FOUND_USER_ERROR);
            }

            Jws<Claims> jwsClaims = getDecodeToken(token);
            Object jwsClaimsString = jwsClaims.getBody().get(GlobalConstants.TOKEN_JWTID);

            Gson gson = new Gson();
            authTokenVO = gson.fromJson((String) jwsClaimsString, AuthTokenVO.class);

            if (authTokenVO == null) {
                log.warn("Token data is invaliad! : {}", token);
                throw new BizExceptionMessage(ErrorType.NOT_FOUND_USER_ERROR);
            }

            //inputTokenVO.getUgpGrpCods() added, integrity check
            SeedCipher sc = new SeedCipher();

            if(!authTokenVO.getUuid().equals(sc.SHA256(getRemoteIp() + authTokenVO.getUserLevel() ))){
                log.warn("Token uui is invaliad! : {}", token);
                throw new BizExceptionMessage(ErrorType.NOT_FOUND_USER_ERROR);
            }
            Calendar today = Calendar.getInstance();
            today.setTime(new Date());

            if (authTokenVO.getExpireDate().compareTo(today.getTime()) == -1) {
                log.warn("token is expired {}", authTokenVO.getExpireDate());
                throw new BizExceptionMessage(ErrorType.NOT_FOUND_USER_ERROR);
            }

        }
        catch(Exception e){
            log.warn("Exception : {}", e.getMessage());
            throw new BizExceptionMessage(ErrorType.NOT_FOUND_USER_ERROR);
        }
        return authTokenVO;
    }

    public String getToken(UserInfoVO userInfoVO){

        AuthTokenVO authTokenVO = new AuthTokenVO();
        String jwtString = "";
        Gson gson = new Gson();

        try{
            SeedCipher sc = new SeedCipher();
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.HOUR, expireHour);

            userInfoVO.setUidLocale(Locale.US);

            authTokenVO.setUserId(userInfoVO.getUidCode() );
            authTokenVO.setUserLevel(userInfoVO.getUidLevel() );
            authTokenVO.setExpireDate(cal.getTime());
            authTokenVO.setUserLocale(userInfoVO.getUidLocale());
            authTokenVO.setUuid(sc.SHA256(getRemoteIp() +  authTokenVO.getUserLevel()  ));


            //byte[] cmkByte = sc.getCleintKey(bizportalDomainCode.getBytes(GlobalConstants.DEFAULT_CHARSET),
            //        bizportalDomainSmKey.getBytes(GlobalConstants.DEFAULT_CHARSET));
            //String cmk = sc.getHex(cmkByte);
            //String authToken =sc.encryptData(gson.toJson(authTokenVO), cmk.getBytes(GlobalConstants.DEFAULT_CHARSET));
            String authToken = gson.toJson(authTokenVO);
            log.info("authToken :: {}", authToken);
            //log.info("descrypt token : {}", sc.decryptAsString(sc.getByteFromHexString(authToken), cmk.getBytes(GlobalConstants.DEFAULT_CHARSET)));
            jwtString = Jwts.builder()
                    .setHeaderParam("tpy","JWT")
                    .claim(GlobalConstants.TOKEN_JWTID, authToken)
                    .signWith(SignatureAlgorithm.HS256, GlobalConstants.JWT_USER_KEY)
                    .compact();

        }catch (Exception e) {
            log.warn("Exception : {}", e.getMessage());
            throw new BizExceptionMessage(ErrorType.NOT_FOUND_USER_ERROR, userInfoVO.getUidLocale());
        }

        return jwtString;
    }

    public String getRemoteIp(){
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

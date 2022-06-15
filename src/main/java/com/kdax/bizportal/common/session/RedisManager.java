package com.kdax.bizportal.common.session;

import com.google.gson.Gson;
import com.kdax.bizportal.common.constants.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisManager {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    VerifyToken verifyToken;

    public void setValue(String key, String value) {
        this.setValue(key, value, GlobalConstants.DEFALUT_REDIS_TIMEOUT_USERINFO);
    }

    public void setValue(String key, String value, int timeout) {
        ValueOperations<String, String> vop = stringRedisTemplate.opsForValue();
        //set
        vop.set(key, value);
        stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public void setValueNonExpire(String key, String value) {
        ValueOperations<String, String> vop = stringRedisTemplate.opsForValue();
        //set
        vop.set(key, value);
    }

    public void setUserSessionInfo(UserRedisInfoVO userRedisInfoVO) {
        ValueOperations<String, String> vop = stringRedisTemplate.opsForValue();

        String value = this.getValue(userRedisInfoVO.getUserCodeId());
        if (value != null) {
            UserRedisInfoVO userRedisInfoVO1 = new Gson().fromJson(value, UserRedisInfoVO.class);
            userRedisInfoVO.setIsOtp(userRedisInfoVO1.getIsOtp());
        }

        vop.set(userRedisInfoVO.getUserCodeId(), new Gson().toJson(userRedisInfoVO));
    }

    public void setOtpAuthenticatedUser(UserRedisInfoVO userRedisInfoVO) {
        ValueOperations<String, String> vop = stringRedisTemplate.opsForValue();

        String value = this.getValue(userRedisInfoVO.getUserCodeId());
        if (value != null) {
            UserRedisInfoVO userRedisInfoVO1 = new Gson().fromJson(value, UserRedisInfoVO.class);
            userRedisInfoVO.setMenuPriviledge(userRedisInfoVO1.getMenuPriviledge());
        }

        vop.set(userRedisInfoVO.getUserCodeId(), new Gson().toJson(userRedisInfoVO));
        stringRedisTemplate.expire(userRedisInfoVO.getUserCodeId(),
                GlobalConstants.DEFALUT_REDIS_TIMEOUT_USERINFO,
                TimeUnit.SECONDS
        );
    }

    public UserRedisInfoVO getUserSessionInfo(String userCodeId) {
        String value = this.getValue(userCodeId);
        return new Gson().fromJson(value, UserRedisInfoVO.class);
    }

    public String getValue(String key) {
        ValueOperations<String, String> vop = stringRedisTemplate.opsForValue();
        stringRedisTemplate.expire(key, GlobalConstants.DEFALUT_REDIS_TIMEOUT_USERINFO, TimeUnit.SECONDS);
        return vop.get(key);
    }

    public String getValueNonExpire(String key) {
        ValueOperations<String, String> vop = stringRedisTemplate.opsForValue();
        return vop.get(key);
    }

    public void setKeyValue(String key, String value, int timeOut) {
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        setOps.add(key, value);
        redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
    }

    public Boolean checkSetValue(String key, String token) {
        AuthTokenVO authVo = verifyToken.getAuthTokenFromString(token);
        if(ObjectUtils.isEmpty(authVo)){
            return false;
        }

        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        return setOps.isMember(key, authVo.getUserCodeId());
    }
}

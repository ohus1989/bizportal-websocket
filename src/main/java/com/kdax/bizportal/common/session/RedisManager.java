package com.kdax.bizportal.common.session;

import com.google.gson.Gson;
import com.kdax.bizportal.common.constants.GlobalConstants;
import com.kdax.bizportal.common.voCommon.MenuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisManager {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void setValue(String key, String value){
        this.setValue(key,value,GlobalConstants.DEFALUT_REDIS_TIMEOUT_USERINFO);
        return;
    }

    public void setValue(String key, String value,int timeout){
        ValueOperations<String, String> vop = stringRedisTemplate.opsForValue();
        //set
        vop.set(key, value);
        stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        return;
    }
    public void setValueNonExpire(String key, String value){
        ValueOperations<String, String> vop = stringRedisTemplate.opsForValue();
        //set
        vop.set(key, value);
        return;
    }

    public void setUserSessionInfo(String userCodeId, List<MenuVO> simpleList){
        ValueOperations<String, String> vop = stringRedisTemplate.opsForValue();
        //자료형 생성
        UserRedisInfoVO setData = new UserRedisInfoVO();
        setData.setId(userCodeId);
        setData.setMenuPriviledge(simpleList);

        //set
        vop.set(userCodeId, new Gson().toJson(setData));
        stringRedisTemplate.expire(userCodeId, GlobalConstants.DEFALUT_REDIS_TIMEOUT_USERINFO, TimeUnit.SECONDS);
        
        log.info("set Redis UserInfo : " + userCodeId);
        return;
    }

    public String getValue(String key){
        ValueOperations<String, String> vop = stringRedisTemplate.opsForValue();
        stringRedisTemplate.expire(key, GlobalConstants.DEFALUT_REDIS_TIMEOUT_USERINFO, TimeUnit.SECONDS);
        return vop.get(key);
    }

}

package com.kdax.bizportal.common.session;

import com.kdax.bizportal.common.constants.GlobalConstants;
import com.kdax.bizportal.common.voCommon.MenuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisManager {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public void setValue(String key, Object value){
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        //set
        vop.set(key, value);
        redisTemplate.expire(key, GlobalConstants.DEFALUT_REDIS_TIMEOUT_USERINFO, TimeUnit.SECONDS);
        return;
    }

    public void setUserSessionInfo(String userCodeId, List<MenuVO> simpleList){
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        //자료형 생성
        UserRedisInfoVO setData = new UserRedisInfoVO();
        setData.setId(userCodeId);
        setData.setMenuPriviledge(simpleList);

        //set
        vop.set(userCodeId, setData);
        redisTemplate.expire(userCodeId, GlobalConstants.DEFALUT_REDIS_TIMEOUT_USERINFO, TimeUnit.SECONDS);
        
        log.info("set Redis UserInfo : " + userCodeId);
        return;
    }

    public Object getValue(String key){
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        redisTemplate.expire(key, GlobalConstants.DEFALUT_REDIS_TIMEOUT_USERINFO, TimeUnit.SECONDS);
        return vop.get(key);
    }

}

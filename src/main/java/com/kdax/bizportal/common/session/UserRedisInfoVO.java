package com.kdax.bizportal.common.session;

import com.kdax.bizportal.common.voCommon.MenuVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RedisHash("UserRedisInfo")
public class UserRedisInfoVO implements Serializable {

    private String id;
    private List<MenuVO> menuPriviledge;
    private LocalDateTime refreshTime;

    public UserRedisInfoVO(){
    }

    public UserRedisInfoVO(String id, LocalDateTime refreshTime) {
        this.id = id;
        this.refreshTime = refreshTime;
    }

    public void refresh(long amount, LocalDateTime refreshTime){
        if(refreshTime.isAfter(this.refreshTime)){ // 저장된 데이터보다 최신 데이터일 경우
            this.refreshTime = refreshTime;
        }
    }

}

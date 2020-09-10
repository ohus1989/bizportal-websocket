package com.kdax.bizportal.common.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class IsEmptyUtil {
    /*
     * 공백 또는 null 체크
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) return true;
        if ((obj instanceof String) && (((String) obj).trim().length() == 0)) {
            return true;
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj instanceof List) {
            return ((List<?>) obj).isEmpty();
        }
        if (obj instanceof Object[]) {
            return ((Object[]) obj).length == 0;
        }
        return false;
    }

    public static boolean mapContainValueEmpty(Map map , String key){
        if(!isEmpty(map) && map.containsKey(key) && map.get(key) !=null)
            return false;
        return true;
    }
}

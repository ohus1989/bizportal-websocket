package com.kdax.bizportal.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@UtilityClass
@Slf4j
public class VoConverterUtil {
    /**
     * 모든 항목 맵에 추가
     * @param paramObj
     * @return
     */
    public static Map voToMap(Object paramObj) {
        Class vo = paramObj.getClass();
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

        for (Field tmpField : vo.getDeclaredFields()) {
            String val = (String)getValueFromField(tmpField, vo, paramObj);
            paramMap.add(tmpField.getName(), val);
            log.debug("Field Name : " + tmpField.getName() + "Field Value : " + val);
        }

        return paramMap;
    }

    /**
     * 값이 있는 항목만 맵에 추가
     * @param paramObj
     * @return
     */
    public static Map voToMapNotEmpty(Object paramObj) {
        Class vo = paramObj.getClass();
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

        for (Field tmpField : vo.getDeclaredFields()) {
            String val = (String)getValueFromField(tmpField, vo, paramObj);
            if(val!=null && !val.equals(""))
            paramMap.add(tmpField.getName(), val);
            log.debug("Field Name : " + tmpField.getName() + "Field Value : " + val);
        }

        return paramMap;
    }

    private Object getValueFromField(Field field, Class<?> clazz, Object obj) {
        for(Method method : clazz.getMethods()) {
            String methodName = method.getName();
            if( (methodName.startsWith("get") && methodName.length() == field.getName().length() + 3)
                    || (methodName.startsWith("is") && methodName.length() == field.getName().length() + 2) ) {

                if(methodName.toLowerCase().endsWith(field.getName().toLowerCase())) {
                    try {
                        return method.invoke(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return null;
    }
}

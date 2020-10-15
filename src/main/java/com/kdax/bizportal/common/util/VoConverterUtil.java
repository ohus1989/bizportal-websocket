package com.kdax.bizportal.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@UtilityClass
@Slf4j
public class VoConverterUtil {
    private static final String[] notExistArr = new String[]{"class"};
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


    public static Map domainToMap(Object vo) throws Exception {
        return domainToMapWithExcept(vo, notExistArr);
//        return domainToMapWithExcept(vo, null);
    }

    public static Map<String, Object> domainToMapWithExcept(Object vo, String[] arrExceptList) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        BeanInfo info = Introspector.getBeanInfo(vo.getClass());
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            Method reader = pd.getReadMethod();
            if (reader != null) {
                if(arrExceptList != null && arrExceptList.length > 0 && isContain(arrExceptList, pd.getName())) continue;
                result.put(pd.getName(), reader.invoke(vo));
            }
        }
        return result;
    }

    public static Boolean isContain(String[] arrList, String name) {
        for (String arr : arrList) {
            if (StringUtils.contains(arr, name))
                return true;
        }
        return false;
    }

    /**
     * Map을 Vo로 변환
     * @param map
     * @param obj
     * @return
     */
    public static Object convertMapToObject(Map<String,Object> map,Object obj){
        String keyAttribute = null;
        String setMethodString = "set";
        String methodString = null;
        Iterator itr = map.keySet().iterator();

        while(itr.hasNext()){
            keyAttribute = (String) itr.next();
            methodString = setMethodString+keyAttribute.substring(0,1).toUpperCase()+keyAttribute.substring(1);
            Method[] methods = obj.getClass().getDeclaredMethods();
            for(int i=0;i<methods.length;i++){
                if(methodString.equals(methods[i].getName())){
                    try{
                        methods[i].invoke(obj, map.get(keyAttribute));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }
}

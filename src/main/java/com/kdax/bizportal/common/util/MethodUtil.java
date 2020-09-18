package com.kdax.bizportal.common.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class MethodUtil {
    public static void setMethod(Object domain, Map<String, ?> map, String escapeKey){

        Field[] fields = domain.getClass().getDeclaredFields();

        Set<String> keySet = map.keySet();
        Iterator<String> iterator = keySet.iterator();
        String key = null;
        Object value = null;

        String newColumnName = null;
//        Method method = null;

        while (iterator.hasNext()) {
            try{
                key = iterator.next();

                if(escapeKey != null){
                    if(key.equals(escapeKey)){
                        continue;
                    }
                }

                value = map.get(key);

                newColumnName = TypeConvertUtil.firstOnlyUpperCase(key);

                if(value instanceof java.lang.String){
                    try{
                        methodInvoke(domain, value, newColumnName, String.class);
                    }catch(Exception e){
                        System.out.println("=====>set" + newColumnName + "(String) err");
                    }
                }

                if(value instanceof java.lang.Integer){
                    try{
                        methodInvoke(domain, value, newColumnName, int.class);
                    }catch(Exception e){
                        System.out.println("=====>set" + newColumnName + "(Integer) err");
                    }
                }

                if(value instanceof java.lang.Double){
                    try{
                        methodInvoke(domain, value, newColumnName, double.class);
                    }catch(Exception e){
                        System.out.println("=====>set" + newColumnName + "(Double) err");
                    }
                }

                if(value instanceof java.util.Date){
                    try{
                        methodInvoke(domain, value, newColumnName, java.util.Date.class);
                    }catch(Exception e){
                        System.out.println("=====>set" + newColumnName + "(Date) err");
                    }
                }
            }catch(Exception e){
                System.out.println("=====>set" + key + "() err");
            }
        }
    }

    private void methodInvoke(Object domain, Object value, String newColumnName, Class clazz ) throws Exception{
        Method method = null;
        method =  domain.getClass().getMethod(
                "set" + newColumnName,
                new Class[] { clazz });
        method.invoke(domain,
                new Object[] { value });
    }
}
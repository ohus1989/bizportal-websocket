package com.kdax.bizportal.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@UtilityClass
@Slf4j
public class MethodUtil {

    private static final String SET_TXT = "=====>set";

    public static void setMethod(Object domain, Map<String, ?> map, String escapeKey){

        Set<String> keySet = map.keySet();
        Iterator<String> iterator = keySet.iterator();
        String key = null;
        Object value = null;

        String newColumnName = null;

        while (iterator.hasNext()) {
            key = iterator.next();

            if (escapeKey != null && key.equals(escapeKey)) {
                continue;
            }

            value = map.get(key);

            newColumnName = TypeConvertUtil.firstOnlyUpperCase(key);

            try {
                if (value instanceof java.lang.String) {
                    methodInvoke(domain, value, newColumnName, String.class);
                } else if (value instanceof java.lang.Integer) {
                    methodInvoke(domain, value, newColumnName, int.class);
                } else if (value instanceof java.lang.Double) {
                    methodInvoke(domain, value, newColumnName, double.class);
                } else if (value instanceof java.util.Date) {
                    methodInvoke(domain, value, newColumnName, java.util.Date.class);
                }
            } catch (Exception e) {
                log.error("{} {} ({}) err", SET_TXT, newColumnName, value.getClass());
            }
        }
    }

    private void methodInvoke(Object domain, Object value, String newColumnName, Class clazz ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = null;
        method =  domain.getClass().getMethod(
                "set" + newColumnName,
                new Class[] { clazz });
        method.invoke(domain,
                new Object[] { value });
    }
}
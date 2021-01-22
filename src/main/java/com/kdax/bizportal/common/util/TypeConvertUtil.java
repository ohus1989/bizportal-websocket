package com.kdax.bizportal.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TypeConvertUtil {

    public static String firstOnlyUpperCase(String str){
        StringBuilder sb = new StringBuilder();
        if(str != null && str.length()>0){
            sb.append(str.substring(0,1).toUpperCase());
            sb.append(str.substring(1));
        }
        return sb.toString();
    }
}
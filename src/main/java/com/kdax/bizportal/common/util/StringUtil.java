package com.kdax.bizportal.common.util;

import lombok.experimental.UtilityClass;

/**
 * @author : ohus1989
 * @packageName :
 * @fileName : StringUtil
 * @date : 2021-01-14 오후 3:18
 * @description :
 * ===========================================================
 * DATE AUTHOR NOTE
 * -----------------------------------------------------------
 * 2021-01-14 오후 3:18 ohus1898 최초 생성
 */
@UtilityClass
public class StringUtil {
    public static String camelCaseToSnakeCase(String targetStr){
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";

        return targetStr.replaceAll(regex, replacement)
                .toUpperCase();
    }
}

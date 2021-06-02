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
    public static String camelCaseToSnakeCase(String targetStr) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";

        return targetStr.replaceAll(regex, replacement)
                .toUpperCase();
    }

    public static String setLPad(String strContext, int iLen, String strChar) {
        String strResult = "";
        StringBuilder sbAddChar = new StringBuilder();
        for (int i = strContext.length(); i < iLen; i++) { // iLen길이 만큼 strChar문자로 채운다.
            sbAddChar.append(strChar);
        }
        strResult = sbAddChar + strContext; // LPAD이므로, 채울문자열 + 원래문자열로 Concate한다.
        return strResult;
    }
}

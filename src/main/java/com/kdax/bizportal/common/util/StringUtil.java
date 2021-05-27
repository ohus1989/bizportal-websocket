package com.kdax.bizportal.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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

    public static <T> T coalesce(T... params)
    {
        for (T param : params)
            if (param != null)
                return param;
        return null;
    }

    public static String toJson(Object obj) {
        return StringUtil.toJson(obj,false);
    }

    public static String toJson(Object obj,boolean isPretty)
    {
        Gson gson = new Gson();
        if(isPretty){
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
        }
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(gson.toJson(obj));
        return gson.toJson(je);
    }


}

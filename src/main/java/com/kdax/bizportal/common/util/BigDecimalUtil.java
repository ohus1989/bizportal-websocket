package com.kdax.bizportal.common.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class BigDecimalUtil {
    public static BigDecimal add(BigDecimal... addends){
        BigDecimal sum = BigDecimal.ZERO;
        if (addends != null) {
            for (BigDecimal addend : addends) {
                if (addend == null) {
                    addend = BigDecimal.ZERO;
                }
                sum = sum.add(addend);
            }
        }
        return sum;
    }

    public static BigDecimal subtract(BigDecimal... addends){
        BigDecimal sum = BigDecimal.ZERO;
        if (addends != null) {
            for(int i = 0; i < addends.length; i++){
                BigDecimal addend = addends[i];
                if (addend == null) {
                    addend = BigDecimal.ZERO;
                }
                if(i == 0){
                    sum = addend;
                }else{
                    sum = sum.subtract(addend);
                }
            }
        }
        return sum;
    }
}

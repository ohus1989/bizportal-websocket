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
}

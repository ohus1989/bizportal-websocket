package com.kdax.bizportal.common.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveField {
    String encrypt() default "FALSE";
    String decrypt() default "FALSE";
}

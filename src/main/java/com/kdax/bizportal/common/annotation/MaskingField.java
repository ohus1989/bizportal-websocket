package com.kdax.bizportal.common.annotation;

import com.kdax.bizportal.common.util.MaskingType;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaskingField{
    MaskingType maskingType();
}

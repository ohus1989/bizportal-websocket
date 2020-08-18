package com.kdax.bizportal.common.annotation;

import com.kdax.bizportal.common.enums.AccessScopeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AcessScope {
    AccessScopeType scope();
}

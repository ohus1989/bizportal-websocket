package com.kdax.bizportal.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = BizPasswordValidator.class)
public @interface BizPassword {

    String message() default "비번은 숫자, 알파벳소문자, !@#$%^&*() 이 최소 하나는 포함되어야 하며 8자 이상";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
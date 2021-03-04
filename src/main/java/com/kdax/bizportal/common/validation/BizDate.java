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
@Constraint(validatedBy = BizDateValidator.class)
public @interface BizDate {

    String message() default "올바른 날짜가 아닙니다.";

    String pattern() default "yyyyMMdd";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
package com.kdax.bizportal.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

public class BizDateValidator implements ConstraintValidator<BizDate, String> {

    private String pattern;
    @Override public void initialize(BizDate constraintAnnotation) {
        pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        SimpleDateFormat df1 = new SimpleDateFormat(pattern);
        df1.setLenient(false);
        try {
            df1.parse(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

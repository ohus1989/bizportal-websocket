package com.kdax.bizportal.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class BizPasswordValidator implements ConstraintValidator<BizPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Pattern.matches("^(?=.*[a-z])(?=.*[0-9])(?=.*[#?!@$%^&*-]).{8,}$", value);
    }
}

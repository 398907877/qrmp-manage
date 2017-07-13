package com.yanyan.core.spring.validator.constraintvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

/**
 * email验证器
 * User: Saintcy
 * Date: 2016/9/25
 * Time: 10:17
 */
public class EmailValidator implements ConstraintValidator<Pattern, String> {
    private String regexp = "";
    private java.util.regex.Pattern pattern;

    public void initialize(Pattern parameters) {
        javax.validation.constraints.Pattern.Flag[] flags = parameters.flags();
        int intFlag = 0;
        for (javax.validation.constraints.Pattern.Flag flag : flags) {
            intFlag = intFlag | flag.getValue();
        }
        try {
            pattern = java.util.regex.Pattern.compile(regexp, intFlag);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Invalid regular expression.");
        }
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}

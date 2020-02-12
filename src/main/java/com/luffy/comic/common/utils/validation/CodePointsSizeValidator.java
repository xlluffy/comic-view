package com.luffy.comic.common.utils.validation;

import com.luffy.comic.common.utils.validation.constrations.CodePointSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CodePointsSizeValidator implements ConstraintValidator<CodePointSize, String> {
    private int min;
    private int max;

    @Override
    public void initialize(CodePointSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) // null值不校检
            return true;
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            if (value.codePointAt(i) < 256) {
                count++;
            } else {
                count += 2;
            }
        }
        return count >= min && count <= max;
    }
}

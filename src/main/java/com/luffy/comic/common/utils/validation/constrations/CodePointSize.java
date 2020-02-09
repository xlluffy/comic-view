package com.luffy.comic.common.utils.validation.constrations;

import com.luffy.comic.common.utils.validation.CodePointsSizeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义验证器，把非ASCII字符视为2个字节
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CodePointsSizeValidator.class)
public @interface CodePointSize {
    int min() default 0;
    int max() default Integer.MAX_VALUE;

    String message() default "{javax.validation.constraints.Size.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

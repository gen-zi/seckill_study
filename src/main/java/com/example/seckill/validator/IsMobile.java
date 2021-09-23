package com.example.seckill.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author lgy
 * @Description JSR303验证自定义的手机号验证注解
 * @ClassName IsMobile
 * @data 2021/9/15
 * @Version 1.0
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {IsMobileValidator.class})
public @interface IsMobile {
    boolean required() default true;//是否是必填

    String message() default "该号码为非法号码";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}

package com.example.seckill.validator;

import com.example.seckill.utils.ValidatorUtil;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author lgy
 * @Description mobile的验证规则
 * @ClassName IsMobileValidator
 * @data 2021/9/15
 * @Version 1.0
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    //判断mobile是否是必填的标志
    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required){
            //必填就直接验证
            return ValidatorUtil.isMobile(value);
        }else{
            //非必填如果是空，那也没错，返回true
            if (StringUtils.isEmpty(value)){
                return true;
            }else{
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}

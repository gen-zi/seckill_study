package com.example.seckill.vo;

import com.example.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author lgy
 * @Description
 * @ClassName LoginBean
 * @data 2021/9/14
 * @Version 1.0
 */
@Data
public class LoginBean {

    @NotNull
    @IsMobile
    private String mobile;
    @NotNull
    @Length(min = 32)
    private String password;
}

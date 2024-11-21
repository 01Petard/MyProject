package com.hzx.model.dto;

import lombok.Data;

@Data
public class SendCodeDTO {
    /**
     * 手机号
     */
    private String phone;
    /**
     * 验证码类型 ： 1-注册，2-登录，3-修改手机号
     */
    private Integer type;

}
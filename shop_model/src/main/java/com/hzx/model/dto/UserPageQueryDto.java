package com.hzx.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPageQueryDto implements Serializable {

    //查询的用户姓名
    private String username;

    //查询的用户手机号
    private String phone;

    //查询的用户邮箱
    private String email;

    //页数（哪一页）
    private int page;

    //每页显示记录数
    private int pageSize;

}

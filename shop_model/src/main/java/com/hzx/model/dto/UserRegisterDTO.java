package com.hzx.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterDTO implements Serializable {

    private String username;

    private String password;

    private String password_confirmed;

    private String email;

    private String phone;

    private String code;

}

package com.hzx.service;

import com.hzx.model.dto.UserLoginDTO;
import com.hzx.model.vo.UserLoginVO;

public interface MessageService {


    void sendRegisterMessage(String phone);


    void sendKeyMessage(String phone, String key);

    void deleteKeyMessage(String phone, String key);
}

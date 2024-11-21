package com.hzx.service;

import com.hzx.common.result.PageResult;
import com.hzx.model.dto.*;
import com.hzx.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzx.model.vo.UserLoginVO;

/**
* @author hzx
* @description 针对表【user】的数据库操作Service
* @createDate 2024-04-24 15:52:34
*/
public interface UserService extends IService<User> {

    void register(UserRegisterDTO userRegisterDTO);

    UserLoginVO login(UserLoginDTO userLoginDTO);

    PageResult pageQuery(UserPageQueryDto userPageQueryDto);

    User findByPhone(String phone);

    User createUser(String phone);

}

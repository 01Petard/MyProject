package com.hzx;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzx.mapper.UserMapper;
import com.hzx.model.entity.User;
import com.hzx.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Test_Mysql {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Test
    public void findByPhone(){
        String phone  = "18357620820";

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));

        System.out.println(user);

    }

    @Test
    public void findByPhone2(){
        String phone  = "18357620820";

        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));

        System.out.println(user);

    }


}

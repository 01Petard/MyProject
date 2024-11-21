package com.hzx;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzx.mapper.UserMapper;
import com.hzx.model.dto.UserRegisterDTO;
import com.hzx.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
class Tests_HzxShopApplication {

    @Test
    void generateRandomNumber() {

        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        String randomNumberString = stringBuilder.toString();
        System.out.println(randomNumberString);

    }

    @Test
    void generateRandomNumber2() {

        System.out.println(
                IntStream.generate(() -> new Random().nextInt(10))
                        .limit(6)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining())
        );

    }


    @Resource
    private UserMapper userMapper;

    @Test
    void addUser() {

        User user = new User();
        user.setUsername("123");
        user.setEmail("123@qq.com");
        user.setPassword("123456");
        user.setPhone("13248684099");
        int insert = userMapper.insert(user);
        System.out.println(insert);

    }


    @Test
    void selectUser() {

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("123");

        User user2 = userMapper.selectOne(new QueryWrapper<User>().eq("username", "123"));
        Integer count2 = userMapper.selectCount(new QueryWrapper<User>().eq("username", "123"));

        userMapper.deleteById(3);

        System.out.println(count2);
        System.out.println(user2);


    }


}

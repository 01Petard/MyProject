package com.hzx.controller.v1;

import com.hzx.common.annotation.HzxLog;
import com.hzx.common.context.BaseContext;
import com.hzx.common.result.PageResult;
import com.hzx.common.result.Result;
import com.hzx.model.dto.UserLoginDTO;
import com.hzx.model.dto.UserPageQueryDto;
import com.hzx.model.dto.UserRegisterDTO;
import com.hzx.model.dto.UserUpdateDTO;
import com.hzx.model.entity.User;
import com.hzx.model.vo.UserLoginVO;
import com.hzx.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册，参数为：{}", userRegisterDTO);
        userService.register(userRegisterDTO);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录，参数为：{}", userLoginDTO);
        UserLoginVO userLoginVO = userService.login(userLoginDTO);
        return Result.success(userLoginVO);
    }


    @GetMapping("/list")
    @HzxLog
    public Result<PageResult> page(@RequestBody UserPageQueryDto userPageQueryDto) {
        log.info("用户分页查询，参数为：{}", userPageQueryDto);
        PageResult pageResult = userService.pageQuery(userPageQueryDto);
        return Result.success(pageResult);
    }


    @PutMapping("/update")
    public Result<User> update(@RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("用户更新，参数为：{}", userUpdateDTO);
        Long user_id = BaseContext.getCurrentId();
        User user_db = userService.getById(user_id);
        BeanUtils.copyProperties(userUpdateDTO, user_db);
        userService.saveOrUpdate(user_db);
        return Result.success(user_db);
    }


    /*
    查询参数
    localhost:8008/user/delete?id=1
     */
    @DeleteMapping("/delete")
    public Result deleteUserById_1(@RequestParam(value = "id") Long userId) {
        log.info("用户删除，参数为：{}", userId);
        userService.removeById(userId);
        return Result.success();
    }

    /*
    路径参数，RESTful风格
    localhost:8008/user/delete/1
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteUserById_2(@PathVariable(value = "id") Long userId) {
        log.info("用户删除，参数为：{}", userId);
        userService.removeById(userId);
        return Result.success();
    }


}

package com.hzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hzx.common.constant.MessageConstant;
import com.hzx.common.constant.RedisKeyConstant;
import com.hzx.common.context.BaseContext;
import com.hzx.common.exception.LoginFailedException;
import com.hzx.common.exception.RedisErrorException;
import com.hzx.common.exception.UserNotExistException;
import com.hzx.common.exception.UserRegisterFailedException;
import com.hzx.common.result.PageResult;
import com.hzx.common.utils.JwtUtil;
import com.hzx.mapper.UserMapper;
import com.hzx.model.dto.UserLoginDTO;
import com.hzx.model.dto.UserPageQueryDto;
import com.hzx.model.dto.UserRegisterDTO;
import com.hzx.model.entity.User;
import com.hzx.model.vo.UserLoginVO;
import com.hzx.service.MessageService;
import com.hzx.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hzx
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-04-24 15:52:34
 */
@Service
@Transactional
@Slf4j
//@RequiredArgsConstructor  final
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private StringRedisTemplate redisTemplate;

    @Resource
    private MessageService messageService;


    @Override
    public void register(UserRegisterDTO userRegisterDTO) {

        if (StringUtils.isNoneBlank(userRegisterDTO.getUsername()) || StringUtils.isNoneBlank(userRegisterDTO.getPassword())) {
            //如果数据库中存在相同用户名的用户，则抛出异常

            Integer db_count = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getUsername, userRegisterDTO.getUsername()));
            if (db_count > 0) {
                throw new UserRegisterFailedException(MessageConstant.USER_ALREADY_EXIST);
            }
            registerByPwd(userRegisterDTO);
        } else if (StringUtils.isNoneBlank(userRegisterDTO.getPhone()) || StringUtils.isNoneBlank(userRegisterDTO.getCode())) {
            //发送用户注册的验证码
            String key_register = RedisKeyConstant.USER_REGISTER + ":" + userRegisterDTO.getPhone();
            registerByMessage(userRegisterDTO, key_register);
        } else {
            throw new UserRegisterFailedException(MessageConstant.REGISTER_FAILED);
        }
    }


    /**
     * 通过用户码 + 密码注册
     * @param userRegisterDTO
     */
    private void registerByPwd(UserRegisterDTO userRegisterDTO) {
        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getPassword_confirmed())) {
            throw new UserRegisterFailedException(MessageConstant.REGISTER_FAILED);
        }

        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);
        userMapper.insert(user);
    }

    /**
     * 通过手机号 + 验证码注册
     * @param userRegisterDTO
     * @param key_register
     */
    private void registerByMessage(UserRegisterDTO userRegisterDTO, String key_register) {

        String redis_code = (String) redisTemplate.opsForValue().get(key_register);
        if (redis_code == null) {
            throw new RedisErrorException(MessageConstant.REDIS_ERROR);
        }
        if (!redis_code.equals(userRegisterDTO.getCode())) {
            throw new UserRegisterFailedException(MessageConstant.MESSAGE_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);
        userMapper.insert(user);
        redisTemplate.delete(key_register);//用户注册完成，删除验证码
    }


    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        UserLoginVO userLoginVO;
        if (StringUtils.isNoneBlank(userLoginDTO.getUsername()) && StringUtils.isNoneBlank(userLoginDTO.getPassword())) {
            userLoginVO = loginByPwd(userLoginDTO);
        } else if (StringUtils.isNoneBlank(userLoginDTO.getPhone()) && StringUtils.isNoneBlank(userLoginDTO.getCode())) {
            //用户登录的验证码
            String key_login = RedisKeyConstant.USER_LOGIN + ":" + BaseContext.getCurrentId() + userLoginDTO.getPhone();

            userLoginVO = loginByMessage(userLoginDTO, key_login);
        } else {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        return userLoginVO;
    }

    @Override
    public PageResult pageQuery(UserPageQueryDto userPageQueryDto) {
        //select * from employess limit 0,10

        //开始分页查询（原理是TreadLocal）
        PageHelper.startPage(userPageQueryDto.getPage(), userPageQueryDto.getPageSize());  //页数（哪一页），每页数量

        //第一种实现方式
//        Page<User> page = userMapper.pageQuery(userPageQueryDto);


        //第二种实现方式
        Page<User> page = (Page<User>) userMapper.selectList(
                Wrappers.<User>lambdaQuery()
                        .like(User::getUsername, userPageQueryDto.getUsername())
                        .like(User::getPhone, userPageQueryDto.getPhone())
                        .like(User::getEmail, userPageQueryDto.getEmail())
                        .orderByAsc(User::getCreatetime)
        );
        //总记录数
        long total = page.getTotal();
        //每页记录数
        int pageSize = page.getPageSize();
        //当前页数据集合
        List<User> records = page.getResult();
        return new PageResult(total, pageSize, records);
    }

    public UserLoginVO loginByPwd(UserLoginDTO userLoginDTO) {
        //判断当前用户是否为新用户，如果是新用户则完成自动注册
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();

        User user = userMapper.selectOne(userLambdaQueryWrapper
                .eq(User::getUsername, userLoginDTO.getUsername())
                .eq(User::getPassword, userLoginDTO.getPassword()));

        //用户不存在，没有注册
        if (user == null) {
            throw new UserNotExistException(MessageConstant.USER_NOT_EXIST);
        }

        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);
        String token = jwtUtil.createJWT_user(user.getId());
        userLoginVO.setToken(token);
        return userLoginVO;
    }

    public UserLoginVO loginByMessage(UserLoginDTO userLoginDTO, String key_login) {
        //判断当前用户是否为新用户，如果是新用户则完成自动注册
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //用户存在就去redis查找该手机号的验证码与用户输入的code是否一致
        User user = userMapper.selectOne(userLambdaQueryWrapper.eq(User::getPhone, userLoginDTO.getPhone()));

        //用户不存在，没有注册
        if (user == null) {
            throw new UserNotExistException(MessageConstant.USER_NOT_EXIST);
        }

        String redis_code = (String) redisTemplate.opsForValue().get(key_login);
        if (redis_code == null) {
            throw new RedisErrorException(MessageConstant.REDIS_ERROR);
        }
        if (!redis_code.equals(userLoginDTO.getCode())) {
            throw new LoginFailedException(MessageConstant.MESSAGE_ERROR);
        }

        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);
        String token = jwtUtil.createJWT_user(user.getId());
        userLoginVO.setToken(token);
        return userLoginVO;
    }


    @Override
    public User findByPhone(String phone) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
    }

    @Override
    public User createUser(String phone) {
        User user = new User();
        user.setPhone(phone);
        userMapper.insert(user);
        return user;
    }


}





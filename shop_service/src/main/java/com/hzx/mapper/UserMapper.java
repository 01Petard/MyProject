package com.hzx.mapper;

import com.github.pagehelper.Page;
import com.hzx.model.dto.UserPageQueryDto;
import com.hzx.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author hzx
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-04-24 15:52:34
* @Entity com.hzx.pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 分页查询
     * @param userPageQueryDto
     * @return
     */
    Page<User> pageQuery(UserPageQueryDto userPageQueryDto);



}





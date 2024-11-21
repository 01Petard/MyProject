package com.hzx.mapper;

import com.hzx.model.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author hzx
* @description 针对表【orders】的数据库操作Mapper
* @createDate 2024-04-24 15:52:34
* @Entity com.hzx.pojo.Orders
*/
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}





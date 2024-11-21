package com.hzx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzx.model.entity.Orders;
import com.hzx.service.OrdersService;
import com.hzx.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author hzx
* @description 针对表【orders】的数据库操作Service实现
* @createDate 2024-04-24 15:52:34
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService{

}





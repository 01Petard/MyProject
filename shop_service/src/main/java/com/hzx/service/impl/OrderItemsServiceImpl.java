package com.hzx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzx.model.entity.OrderItems;
import com.hzx.service.OrderItemsService;
import com.hzx.mapper.OrderItemsMapper;
import org.springframework.stereotype.Service;

/**
* @author hzx
* @description 针对表【order_items】的数据库操作Service实现
* @createDate 2024-04-24 15:52:34
*/
@Service
public class OrderItemsServiceImpl extends ServiceImpl<OrderItemsMapper, OrderItems> implements OrderItemsService{

}





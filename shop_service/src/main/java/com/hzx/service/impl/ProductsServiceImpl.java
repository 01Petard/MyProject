package com.hzx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzx.model.entity.Products;
import com.hzx.service.ProductsService;
import com.hzx.mapper.ProductsMapper;
import org.springframework.stereotype.Service;

/**
* @author hzx
* @description 针对表【products】的数据库操作Service实现
* @createDate 2024-04-24 15:52:34
*/
@Service
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products> implements ProductsService{

}





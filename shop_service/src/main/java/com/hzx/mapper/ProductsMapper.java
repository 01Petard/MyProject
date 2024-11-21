package com.hzx.mapper;

import com.hzx.model.entity.Products;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author hzx
* @description 针对表【products】的数据库操作Mapper
* @createDate 2024-04-24 15:52:34
* @Entity com.hzx.pojo.Products
*/
@Mapper
public interface ProductsMapper extends BaseMapper<Products> {

}





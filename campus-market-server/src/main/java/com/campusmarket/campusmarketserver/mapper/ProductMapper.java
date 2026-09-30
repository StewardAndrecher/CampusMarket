package com.campusmarket.campusmarketserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campusmarket.campusmarketserver.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
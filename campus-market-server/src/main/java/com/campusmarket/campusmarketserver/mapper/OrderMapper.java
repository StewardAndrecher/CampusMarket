package com.campusmarket.campusmarketserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campusmarket.campusmarketserver.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}

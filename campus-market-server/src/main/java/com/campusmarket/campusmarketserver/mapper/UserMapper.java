package com.campusmarket.campusmarketserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campusmarket.campusmarketserver.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 不用写任何 SQL，BaseMapper 已提供 insert/selectById/selectOne 等常用方法
}
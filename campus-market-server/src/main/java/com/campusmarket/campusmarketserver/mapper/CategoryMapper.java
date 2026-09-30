package com.campusmarket.campusmarketserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campusmarket.campusmarketserver.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
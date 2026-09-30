package com.campusmarket.campusmarketserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sellerId;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Long categoryId;
    // 字段名避开保留字 condition，驼峰映射 productCondition ↔ product_condition 自动生效
    private Integer productCondition;   // 成色 0全新 1九成 2八成 3七成以下
    private String location;
    private Integer status;      // 0下架 1在售
    private Integer viewCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
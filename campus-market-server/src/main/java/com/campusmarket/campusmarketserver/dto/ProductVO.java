package com.campusmarket.campusmarketserver.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductVO {
    private Long id;
    private Long sellerId;
    private String sellerNickname;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Long categoryId;
    private Integer condition;
    private String location;
    private Integer status;
    private Integer viewCount;
    private LocalDateTime createTime;
    private String coverUrl;      // 列表用的封面（第一张图）
    private List<String> images;  // 详情用的全部图片
}
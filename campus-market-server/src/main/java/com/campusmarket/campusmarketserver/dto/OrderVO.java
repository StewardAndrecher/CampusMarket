package com.campusmarket.campusmarketserver.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderVO {
    private Long id;
    private String orderNo;
    private Long productId;
    private String productTitle;
    private String productCover;
    private BigDecimal price;
    private Long buyerId;
    private Long sellerId;
    private String sellerNickname;
    private String buyerNickname;
    private Integer status;
    private String statusText;
    private String tradeLocation;
    private String remark;
    private LocalDateTime createTime;
}

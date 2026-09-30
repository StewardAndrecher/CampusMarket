package com.campusmarket.campusmarketserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long productId;
    private String productTitle;
    private String productCover;
    private BigDecimal price;
    private Long buyerId;
    private Long sellerId;
    /** 0待付款 1待交付 2已完成 3已取消 */
    private Integer status;
    private String tradeLocation;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private LocalDateTime finishTime;
}

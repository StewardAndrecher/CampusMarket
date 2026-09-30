package com.campusmarket.campusmarketserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campusmarket.campusmarketserver.dto.OrderCreateRequest;
import com.campusmarket.campusmarketserver.dto.OrderVO;

public interface OrderService {
    OrderVO create(OrderCreateRequest req);
    /** 模拟支付（待付款→待交付） */
    void pay(Long orderId);
    /** 买家确认收货（待交付→已完成） */
    void confirm(Long orderId);
    /** 买家取消（待付款→已取消，商品恢复在售） */
    void cancel(Long orderId);
    IPage<OrderVO> myBuyOrders(int page, int size);
    IPage<OrderVO> mySellOrders(int page, int size);
}

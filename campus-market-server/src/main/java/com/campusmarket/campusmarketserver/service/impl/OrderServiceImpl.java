package com.campusmarket.campusmarketserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campusmarket.campusmarketserver.common.BusinessException;
import com.campusmarket.campusmarketserver.dto.OrderCreateRequest;
import com.campusmarket.campusmarketserver.dto.OrderVO;
import com.campusmarket.campusmarketserver.entity.Order;
import com.campusmarket.campusmarketserver.entity.Product;
import com.campusmarket.campusmarketserver.entity.ProductImage;
import com.campusmarket.campusmarketserver.entity.User;
import com.campusmarket.campusmarketserver.mapper.OrderMapper;
import com.campusmarket.campusmarketserver.mapper.ProductImageMapper;
import com.campusmarket.campusmarketserver.mapper.ProductMapper;
import com.campusmarket.campusmarketserver.mapper.UserMapper;
import com.campusmarket.campusmarketserver.service.OrderService;
import com.campusmarket.campusmarketserver.util.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    private final UserMapper userMapper;

    public OrderServiceImpl(OrderMapper orderMapper, ProductMapper productMapper,
                            ProductImageMapper productImageMapper, UserMapper userMapper) {
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
        this.productImageMapper = productImageMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public OrderVO create(OrderCreateRequest req) {
        Long buyerId = UserContext.getUserId();
        Product p = productMapper.selectById(req.getProductId());
        if (p == null) throw new BusinessException("商品不存在");
        if (p.getStatus() == null || p.getStatus() != 1) throw new BusinessException("商品已售出或已下架");
        if (p.getSellerId().equals(buyerId)) throw new BusinessException("不能买自己的商品");

        Order o = new Order();
        o.setOrderNo(generateOrderNo());
        o.setProductId(p.getId());
        o.setProductTitle(p.getTitle());
        // 封面取第一张图
        ProductImage cover = productImageMapper.selectOne(new LambdaQueryWrapper<ProductImage>()
                .eq(ProductImage::getProductId, p.getId()).orderByAsc(ProductImage::getSort).last("LIMIT 1"));
        o.setProductCover(cover != null ? cover.getUrl() : null);
        o.setPrice(p.getPrice());
        o.setBuyerId(buyerId);
        o.setSellerId(p.getSellerId());
        o.setStatus(0);
        o.setTradeLocation(req.getTradeLocation());
        o.setRemark(req.getRemark());
        orderMapper.insert(o);

        // 商品锁定：状态改为 0（不可再售）
        p.setStatus(0);
        productMapper.updateById(p);

        return toVO(o);
    }

    @Override
    @Transactional
    public void pay(Long orderId) {
        Order o = mustGet(orderId);
        Long me = UserContext.getUserId();
        if (!o.getBuyerId().equals(me)) throw new BusinessException("只能支付自己的订单");
        if (o.getStatus() != 0) throw new BusinessException("订单状态不允许支付");
        o.setStatus(1);
        o.setPayTime(LocalDateTime.now());
        orderMapper.updateById(o);
    }

    @Override
    @Transactional
    public void confirm(Long orderId) {
        Order o = mustGet(orderId);
        Long me = UserContext.getUserId();
        if (!o.getBuyerId().equals(me)) throw new BusinessException("只能确认自己的订单");
        if (o.getStatus() != 1) throw new BusinessException("订单状态不允许确认收货");
        o.setStatus(2);
        o.setFinishTime(LocalDateTime.now());
        orderMapper.updateById(o);
    }

    @Override
    @Transactional
    public void cancel(Long orderId) {
        Order o = mustGet(orderId);
        Long me = UserContext.getUserId();
        if (!o.getBuyerId().equals(me) && !o.getSellerId().equals(me))
            throw new BusinessException("无权取消该订单");
        if (o.getStatus() != 0) throw new BusinessException("只有待付款订单能取消");
        o.setStatus(3);
        orderMapper.updateById(o);
        // 商品恢复在售
        Product p = productMapper.selectById(o.getProductId());
        if (p != null) {
            p.setStatus(1);
            productMapper.updateById(p);
        }
    }

    @Override
    public IPage<OrderVO> myBuyOrders(int page, int size) {
        Long me = UserContext.getUserId();
        Page<Order> p = new Page<>(page, size);
        IPage<Order> result = orderMapper.selectPage(p,
                new LambdaQueryWrapper<Order>().eq(Order::getBuyerId, me).orderByDesc(Order::getId));
        return result.convert(this::toVO);
    }

    @Override
    public IPage<OrderVO> mySellOrders(int page, int size) {
        Long me = UserContext.getUserId();
        Page<Order> p = new Page<>(page, size);
        IPage<Order> result = orderMapper.selectPage(p,
                new LambdaQueryWrapper<Order>().eq(Order::getSellerId, me).orderByDesc(Order::getId));
        return result.convert(this::toVO);
    }

    private Order mustGet(Long id) {
        Order o = orderMapper.selectById(id);
        if (o == null) throw new BusinessException("订单不存在");
        return o;
    }

    private OrderVO toVO(Order o) {
        OrderVO v = new OrderVO();
        v.setId(o.getId());
        v.setOrderNo(o.getOrderNo());
        v.setProductId(o.getProductId());
        v.setProductTitle(o.getProductTitle());
        v.setProductCover(o.getProductCover());
        v.setPrice(o.getPrice());
        v.setBuyerId(o.getBuyerId());
        v.setSellerId(o.getSellerId());
        User seller = userMapper.selectById(o.getSellerId());
        User buyer = userMapper.selectById(o.getBuyerId());
        v.setSellerNickname(seller != null ? seller.getNickname() : "未知");
        v.setBuyerNickname(buyer != null ? buyer.getNickname() : "未知");
        v.setStatus(o.getStatus());
        v.setStatusText(new String[]{"待付款", "待交付", "已完成", "已取消"}[o.getStatus()]);
        v.setTradeLocation(o.getTradeLocation());
        v.setRemark(o.getRemark());
        v.setCreateTime(o.getCreateTime());
        return v;
    }

    private String generateOrderNo() {
        return "CM" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}

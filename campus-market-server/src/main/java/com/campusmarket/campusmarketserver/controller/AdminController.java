package com.campusmarket.campusmarketserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campusmarket.campusmarketserver.common.BusinessException;
import com.campusmarket.campusmarketserver.entity.Order;
import com.campusmarket.campusmarketserver.entity.Product;
import com.campusmarket.campusmarketserver.entity.User;
import com.campusmarket.campusmarketserver.mapper.OrderMapper;
import com.campusmarket.campusmarketserver.mapper.ProductMapper;
import com.campusmarket.campusmarketserver.mapper.UserMapper;
import com.campusmarket.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;

    public AdminController(UserMapper userMapper, ProductMapper productMapper, OrderMapper orderMapper) {
        this.userMapper = userMapper;
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
    }

    /** 数据看板 */
    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        Map<String, Object> m = new HashMap<>();
        m.put("userCount", userMapper.selectCount(null));
        m.put("productCount", productMapper.selectCount(null));
        m.put("onsaleCount", productMapper.selectCount(new LambdaQueryWrapper<Product>().eq(Product::getStatus, 1)));
        m.put("orderCount", orderMapper.selectCount(null));
        // 交易额：已完成订单
        Double total = orderMapper.selectList(new LambdaQueryWrapper<Order>().eq(Order::getStatus, 2))
                .stream().mapToDouble(o -> o.getPrice().doubleValue()).sum();
        m.put("totalAmount", total);
        return Result.ok(m);
    }

    /** 用户列表 */
    @GetMapping("/users")
    public Result<Map<String, Object>> users(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        Page<User> p = userMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<User>().orderByDesc(User::getId));
        p.getRecords().forEach(u -> u.setPassword(null));
        return Result.ok(Map.of("total", p.getTotal(), "records", p.getRecords()));
    }

    /** 禁用/启用用户 */
    @PostMapping("/users/{id}/toggle-status")
    public Result<Void> toggleUser(@PathVariable Long id) {
        User u = userMapper.selectById(id);
        if (u == null) throw new BusinessException("用户不存在");
        u.setStatus(u.getStatus() == 1 ? 0 : 1);
        userMapper.updateById(u);
        return Result.ok(null);
    }

    /** 商品列表（可按状态筛选） */
    @GetMapping("/products")
    public Result<Map<String, Object>> products(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "20") int size,
                                                @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<Product> q = new LambdaQueryWrapper<>();
        if (status != null) q.eq(Product::getStatus, status);
        q.orderByDesc(Product::getId);
        Page<Product> p = productMapper.selectPage(new Page<>(page, size), q);
        return Result.ok(Map.of("total", p.getTotal(), "records", p.getRecords()));
    }

    /** 审核商品：0拒绝 1通过 */
    @PostMapping("/products/{id}/audit")
    public Result<Void> audit(@PathVariable Long id, @RequestParam int status) {
        Product p = productMapper.selectById(id);
        if (p == null) throw new BusinessException("商品不存在");
        p.setStatus(status);
        productMapper.updateById(p);
        return Result.ok(null);
    }

    /** 全部订单 */
    @GetMapping("/orders")
    public Result<Map<String, Object>> orders(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "20") int size) {
        Page<Order> p = orderMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<Order>().orderByDesc(Order::getId));
        return Result.ok(Map.of("total", p.getTotal(), "records", p.getRecords()));
    }
}

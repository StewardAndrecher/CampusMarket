package com.campusmarket.campusmarketserver.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campusmarket.campusmarketserver.common.BusinessException;
import com.campusmarket.campusmarketserver.dto.OrderCreateRequest;
import com.campusmarket.campusmarketserver.dto.OrderVO;
import com.campusmarket.campusmarketserver.service.OrderService;
import com.campusmarket.common.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping("/create")
    public Result<OrderVO> create(@RequestBody @Valid OrderCreateRequest req) {
        return Result.ok(orderService.create(req));
    }

    @PostMapping("/{id}/pay")
    public Result<Void> pay(@PathVariable Long id) {
        orderService.pay(id);
        return Result.ok(null);
    }

    @PostMapping("/{id}/confirm")
    public Result<Void> confirm(@PathVariable Long id) {
        orderService.confirm(id);
        return Result.ok(null);
    }

    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        orderService.cancel(id);
        return Result.ok(null);
    }

    @GetMapping("/buy")
    public Result<IPage<OrderVO>> myBuy(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return Result.ok(orderService.myBuyOrders(page, size));
    }

    @GetMapping("/sell")
    public Result<IPage<OrderVO>> mySell(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return Result.ok(orderService.mySellOrders(page, size));
    }
}

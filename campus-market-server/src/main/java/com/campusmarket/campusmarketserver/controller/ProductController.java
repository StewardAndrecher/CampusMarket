package com.campusmarket.campusmarketserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campusmarket.campusmarketserver.dto.ProductCreateRequest;
import com.campusmarket.campusmarketserver.dto.ProductVO;
import com.campusmarket.campusmarketserver.entity.Category;
import com.campusmarket.campusmarketserver.mapper.CategoryMapper;
import com.campusmarket.campusmarketserver.service.ProductService;
import com.campusmarket.common.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryMapper categoryMapper;

    // 发布商品（需登录，被 LoginInterceptor 拦截）
    @PostMapping("/product/create")
    public Result<ProductVO> create(@RequestBody @Valid ProductCreateRequest request) {
        return Result.ok(productService.createProduct(request));
    }

    // 商品列表（浏览，无需登录）
    @GetMapping("/product/list")
    public Result<Page<ProductVO>> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(productService.listProducts(categoryId, keyword, page, size));
    }

    // 商品详情（无需登录）
    @GetMapping("/product/detail/{id}")
    public Result<ProductVO> detail(@PathVariable Long id) {
        return Result.ok(productService.getProductDetail(id));
    }

    // 分类列表（无需登录）
    @GetMapping("/category/list")
    public Result<List<Category>> categories() {
        // 显式构造空条件，避免 selectList(null) 的泛型推断问题
        return Result.ok(categoryMapper.selectList(new LambdaQueryWrapper<Category>().orderByAsc(Category::getId)));
    }
}

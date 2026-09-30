package com.campusmarket.campusmarketserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campusmarket.campusmarketserver.dto.ProductCreateRequest;
import com.campusmarket.campusmarketserver.dto.ProductVO;

public interface ProductService {

    // 发布商品（从 UserContext 取当前登录用户作为卖家）
    ProductVO createProduct(ProductCreateRequest request);

    // 商品列表：分类过滤 + 关键词 + 分页
    Page<ProductVO> listProducts(Long categoryId, String keyword, int page, int size);

    // 商品详情（浏览量 +1）
    ProductVO getProductDetail(Long id);
}

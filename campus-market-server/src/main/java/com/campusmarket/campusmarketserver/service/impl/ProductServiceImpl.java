package com.campusmarket.campusmarketserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campusmarket.campusmarketserver.common.BusinessException;
import com.campusmarket.campusmarketserver.dto.ProductCreateRequest;
import com.campusmarket.campusmarketserver.dto.ProductVO;
import com.campusmarket.campusmarketserver.entity.Product;
import com.campusmarket.campusmarketserver.entity.ProductImage;
import com.campusmarket.campusmarketserver.entity.User;
import com.campusmarket.campusmarketserver.mapper.ProductImageMapper;
import com.campusmarket.campusmarketserver.mapper.ProductMapper;
import com.campusmarket.campusmarketserver.mapper.UserMapper;
import com.campusmarket.campusmarketserver.service.ProductService;
import com.campusmarket.campusmarketserver.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor   // 自动生成构造器注入 final 字段
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional   // 商品 + 图片要么都成功，要么都回滚
    public ProductVO createProduct(ProductCreateRequest request) {
        // 当前登录用户（拦截器存进 ThreadLocal 的）
        Long sellerId = UserContext.getUserId();

        // 1. 插商品主表
        Product product = new Product();
        product.setSellerId(sellerId);
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setCategoryId(request.getCategoryId());
        product.setProductCondition(request.getCondition());
        product.setLocation(request.getLocation());
        product.setStatus(1);   // 1=在售
        productMapper.insert(product);

        // 2. 插图片（0~n 张）
        if (request.getImages() != null) {
            int sort = 0;
            for (String url : request.getImages()) {
                ProductImage img = new ProductImage();
                img.setProductId(product.getId());
                img.setUrl(url);
                img.setSort(sort++);
                productImageMapper.insert(img);
            }
        }

        return getProductDetail(product.getId());
    }

    @Override
    public Page<ProductVO> listProducts(Long categoryId, String keyword, int page, int size) {
        Page<Product> p = new Page<>(page, size);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<Product>()
                .eq(Product::getStatus, 1)                                     // 只在售
                .eq(categoryId != null, Product::getCategoryId, categoryId)   // 条件拼接：选了分类才过滤
                .like(StringUtils.hasText(keyword), Product::getTitle, keyword)
                .orderByDesc(Product::getCreateTime);                          // 新的在前
        Page<Product> result = productMapper.selectPage(p, wrapper);

        // 转 VO：每件商品查第一张图当封面
        Page<ProductVO> voPage = new Page<>(page, size, result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::toListItemVO).toList());
        return voPage;
    }

    @Override
    public ProductVO getProductDetail(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        // 浏览量 +1
        product.setViewCount(product.getViewCount() == null ? 1 : product.getViewCount() + 1);
        productMapper.updateById(product);

        ProductVO vo = toListItemVO(product);
        // 全部图片（按 sort 排序）
        List<ProductImage> images = productImageMapper.selectList(
                new LambdaQueryWrapper<ProductImage>()
                        .eq(ProductImage::getProductId, id)
                        .orderByAsc(ProductImage::getSort));
        vo.setImages(images.stream().map(ProductImage::getUrl).toList());
        // 卖家昵称
        User seller = userMapper.selectById(product.getSellerId());
        vo.setSellerNickname(seller != null ? seller.getNickname() : "未知用户");
        return vo;
    }

    // 列表项：商品字段 + 封面图（第一张）
    private ProductVO toListItemVO(Product product) {
        ProductVO vo = new ProductVO();
        vo.setId(product.getId());
        vo.setSellerId(product.getSellerId());
        vo.setTitle(product.getTitle());
        vo.setDescription(product.getDescription());
        vo.setPrice(product.getPrice());
        vo.setOriginalPrice(product.getOriginalPrice());
        vo.setCategoryId(product.getCategoryId());
        vo.setCondition(product.getProductCondition());
        vo.setLocation(product.getLocation());
        vo.setStatus(product.getStatus());
        vo.setViewCount(product.getViewCount());
        vo.setCreateTime(product.getCreateTime());
        // 封面 = 第一张图
        ProductImage cover = productImageMapper.selectOne(
                new LambdaQueryWrapper<ProductImage>()
                        .eq(ProductImage::getProductId, product.getId())
                        .orderByAsc(ProductImage::getSort)
                        .last("LIMIT 1"));
        vo.setCoverUrl(cover != null ? cover.getUrl() : null);
        return vo;
    }
}

package com.campusmarket.campusmarketserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campusmarket.campusmarketserver.dto.ProductVO;
import com.campusmarket.campusmarketserver.entity.Favorite;
import com.campusmarket.campusmarketserver.entity.Product;
import com.campusmarket.campusmarketserver.entity.ProductImage;
import com.campusmarket.campusmarketserver.mapper.FavoriteMapper;
import com.campusmarket.campusmarketserver.mapper.ProductImageMapper;
import com.campusmarket.campusmarketserver.mapper.ProductMapper;
import com.campusmarket.campusmarketserver.service.FavoriteService;
import com.campusmarket.campusmarketserver.util.UserContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;

    public FavoriteServiceImpl(FavoriteMapper favoriteMapper, ProductMapper productMapper,
                               ProductImageMapper productImageMapper) {
        this.favoriteMapper = favoriteMapper;
        this.productMapper = productMapper;
        this.productImageMapper = productImageMapper;
    }

    @Override
    public boolean toggle(Long productId) {
        Long uid = UserContext.getUserId();
        Favorite exist = favoriteMapper.selectOne(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, uid).eq(Favorite::getProductId, productId));
        if (exist != null) {
            favoriteMapper.deleteById(exist);
            return false;
        }
        Favorite f = new Favorite();
        f.setUserId(uid);
        f.setProductId(productId);
        favoriteMapper.insert(f);
        return true;
    }

    @Override
    public IPage<ProductVO> myFavorites(int page, int size) {
        Long uid = UserContext.getUserId();
        Page<Favorite> p = new Page<>(page, size);
        IPage<Favorite> fpage = favoriteMapper.selectPage(p,
                new LambdaQueryWrapper<Favorite>().eq(Favorite::getUserId, uid).orderByDesc(Favorite::getId));

        Page<ProductVO> result = new Page<>(page, size, fpage.getTotal());
        List<ProductVO> list = new ArrayList<>();
        for (Favorite f : fpage.getRecords()) {
            Product prod = productMapper.selectById(f.getProductId());
            if (prod != null) {
                ProductVO v = new ProductVO();
                v.setId(prod.getId());
                v.setTitle(prod.getTitle());
                v.setPrice(prod.getPrice());
                ProductImage cover = productImageMapper.selectOne(new LambdaQueryWrapper<ProductImage>()
                        .eq(ProductImage::getProductId, prod.getId()).orderByAsc(ProductImage::getSort).last("LIMIT 1"));
                v.setCoverUrl(cover != null ? cover.getUrl() : null);
                v.setCondition(prod.getProductCondition());
                v.setLocation(prod.getLocation());
                v.setViewCount(prod.getViewCount());
                list.add(v);
            }
        }
        result.setRecords(list);
        return result;
    }

    @Override
    public boolean isFavorited(Long productId) {
        Long uid = UserContext.getUserId();
        return favoriteMapper.selectCount(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, uid).eq(Favorite::getProductId, productId)) > 0;
    }
}

package com.campusmarket.campusmarketserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campusmarket.campusmarketserver.dto.ProductVO;

public interface FavoriteService {
    /** 切换收藏，返回 true=已收藏 false=已取消 */
    boolean toggle(Long productId);
    IPage<ProductVO> myFavorites(int page, int size);
    boolean isFavorited(Long productId);
}

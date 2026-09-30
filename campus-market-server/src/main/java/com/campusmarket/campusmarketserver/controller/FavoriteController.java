package com.campusmarket.campusmarketserver.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campusmarket.campusmarketserver.common.BusinessException;
import com.campusmarket.campusmarketserver.dto.ProductVO;
import com.campusmarket.campusmarketserver.service.FavoriteService;
import com.campusmarket.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;
    public FavoriteController(FavoriteService favoriteService) { this.favoriteService = favoriteService; }

    @PostMapping("/toggle/{productId}")
    public Result<Map<String, Object>> toggle(@PathVariable Long productId) {
        boolean fav = favoriteService.toggle(productId);
        Map<String, Object> m = new HashMap<>();
        m.put("favorited", fav);
        return Result.ok(m);
    }

    @GetMapping("/list")
    public Result<IPage<ProductVO>> list(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return Result.ok(favoriteService.myFavorites(page, size));
    }

    @GetMapping("/check/{productId}")
    public Result<Map<String, Object>> check(@PathVariable Long productId) {
        Map<String, Object> m = new HashMap<>();
        m.put("favorited", favoriteService.isFavorited(productId));
        return Result.ok(m);
    }
}

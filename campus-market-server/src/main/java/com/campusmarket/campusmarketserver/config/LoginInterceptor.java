package com.campusmarket.campusmarketserver.config;

import com.campusmarket.campusmarketserver.common.BusinessException;
import com.campusmarket.campusmarketserver.util.JwtUtil;
import com.campusmarket.campusmarketserver.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 取 Authorization 头：Bearer <token>
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            // 401 = 未登录（HTTP 语义），前端据此跳登录页
            throw new BusinessException("未登录，请先登录", 401);
        }
        // 2. 解析 token 拿 userId（过期/伪造会抛异常，被全局处理器捕获）
        Long userId = JwtUtil.parseUserId(auth.substring(7));
        // 3. 存入 ThreadLocal
        UserContext.setUserId(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        UserContext.clear();
    }
}
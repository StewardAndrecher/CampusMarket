package com.campusmarket.campusmarketserver.config;

import com.campusmarket.campusmarketserver.entity.User;
import com.campusmarket.campusmarketserver.mapper.UserMapper;
import com.campusmarket.campusmarketserver.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final UserMapper userMapper;
    public AdminInterceptor(UserMapper userMapper) { this.userMapper = userMapper; }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        Long userId = UserContext.getUserId();
        User u = userMapper.selectById(userId);
        if (u == null || u.getRole() == null || u.getRole() != 1) {
            resp.setStatus(403);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write("{\"code\":403,\"message\":\"需要管理员权限\"}");
            return false;
        }
        return true;
    }
}

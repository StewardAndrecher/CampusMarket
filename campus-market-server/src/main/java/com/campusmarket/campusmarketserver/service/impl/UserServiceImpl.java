
package com.campusmarket.campusmarketserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campusmarket.campusmarketserver.common.BusinessException;
import com.campusmarket.campusmarketserver.dto.LoginRequest;
import com.campusmarket.campusmarketserver.dto.LoginResponse;
import com.campusmarket.campusmarketserver.dto.RegisterRequest;
import com.campusmarket.campusmarketserver.entity.User;
import com.campusmarket.campusmarketserver.mapper.UserMapper;
import com.campusmarket.campusmarketserver.service.UserService;
import com.campusmarket.campusmarketserver.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor   // 自动生成构造器注入 final 字段
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public LoginResponse register(RegisterRequest request) {
        // 1. 校验用户名是否已存在
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername()));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 2. 组装用户并加密密码
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 加密！
        user.setNickname(request.getNickname() == null || request.getNickname().isBlank()
                ? request.getUsername() : request.getNickname());
        userMapper.insert(user);   // create_time/status 走数据库默认值

        // 3. 注册成功即登录，直接发 token
        return buildLoginResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. 按用户名查用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername()));

        // 2. 用户不存在 或 密码不匹配 → 统一提示（不暴露具体哪个错，防撞库）
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 3. 签发 token
        return buildLoginResponse(user);
    }

    private LoginResponse buildLoginResponse(User user) {
        LoginResponse resp = new LoginResponse();
        resp.setToken(JwtUtil.createToken(user.getId()));
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setNickname(user.getNickname());
        return resp;
    }
}
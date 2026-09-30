package com.campusmarket.campusmarketserver.util;


public class UserContext {
    private static final ThreadLocal<Long> CURRENT_USER = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        CURRENT_USER.set(userId);
    }

    public static Long getUserId() {
        return CURRENT_USER.get();
    }

    public static void clear() {
        CURRENT_USER.remove();   // 请求结束必须清，防线程复用串号
    }
}

package com.campusmarket.campusmarketserver.common;

// 业务异常：业务规则不满足时主动抛出，比如"用户名已存在"
// httpStatus：默认 200（业务失败），未登录传 401（让前端跳登录页）
public class BusinessException extends RuntimeException {
    private final int httpStatus;

    public BusinessException(String message) {
        this(message, 200);
    }

    public BusinessException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}

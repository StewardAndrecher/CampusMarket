
package com.campusmarket.campusmarketserver.common;

// 业务异常：业务规则不满足时主动抛出，比如"用户名已存在"
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
package com.campusmarket.campusmarketserver.common;

import com.campusmarket.common.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice   // 全局捕获 Controller 抛出的异常
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusiness(BusinessException e) {
        // 按 httpStatus 返回：未登录 401，普通业务失败 HTTP 200 + code 500
        return ResponseEntity.status(e.getHttpStatus()).body(Result.error(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValid(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldError();
        String msg = error != null ? error.getDefaultMessage() : "参数错误";
        return Result.error(msg);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleOther(Exception e) {
        // 打完整异常栈，否则"服务器内部错误"会吞掉所有排查线索
        org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class).error("未处理异常", e);
        return Result.error("服务器内部错误");
    }
}

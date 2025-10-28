package com.web3.indexer.common.exception;

import java.util.stream.Collectors;

import com.web3.indexer.common.enums.ResultCode;
import com.web3.indexer.common.result.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常: {} - {}", e.getCode(), e.getMessage());
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常 - @RequestBody @Valid 验证失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("参数校验失败: {}", errorMessage);
        return ApiResponse.error(ResultCode.PARAM_VALIDATION_FAILED.getCode(), errorMessage);
    }

    /**
     * 处理参数绑定异常 - @ModelAttribute 验证失败
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleBindException(BindException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("参数绑定失败: {}", errorMessage);
        return ApiResponse.error(ResultCode.PARAM_VALIDATION_FAILED.getCode(), errorMessage);
    }

    /**
     * 处理参数校验异常 - @RequestParam、@PathVariable 验证失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleConstraintViolationException(ConstraintViolationException e,
            HttpServletRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        log.warn("参数约束违反: {}", errorMessage);
        return ApiResponse.error(ResultCode.PARAM_VALIDATION_FAILED.getCode(), errorMessage);
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Object> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        log.error("空指针异常:", e);
        return ApiResponse.error(ResultCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Object> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: ", e);
        return ApiResponse.error(ResultCode.INTERNAL_SERVER_ERROR);
    }
}

package com.web3.indexer.common.exception;

import java.util.stream.Collectors;

import com.web3.indexer.common.enums.ResultCode;
import com.web3.indexer.common.result.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * WebFlux 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {} - {}", e.getCode(), e.getMessage());
        return Mono.just(ResponseEntity
                .badRequest()
                .body(ApiResponse.error(e.getCode(), e.getMessage())));
    }

    /**
     * 处理参数校验异常 - WebFlux 版本
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleWebExchangeBindException(WebExchangeBindException e) {
        String errorMessage = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("参数校验失败: {}", errorMessage);
        return Mono.just(ResponseEntity
                .badRequest()
                .body(ApiResponse.error(ResultCode.PARAM_VALIDATION_FAILED.getCode(), errorMessage)));
    }

    /**
     * 处理参数校验异常 - @RequestParam、@PathVariable 验证失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleConstraintViolationException(
            ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        log.warn("参数约束违反: {}", errorMessage);
        return Mono.just(ResponseEntity
                .badRequest()
                .body(ApiResponse.error(ResultCode.PARAM_VALIDATION_FAILED.getCode(), errorMessage)));
    }

    /**
     * 处理 ServerWebInputException - WebFlux 中的参数解析异常
     */
    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleServerWebInputException(ServerWebInputException e) {
        log.warn("请求参数解析失败: {}", e.getReason());
        return Mono.just(ResponseEntity
                .badRequest()
                .body(ApiResponse.error(ResultCode.PARAM_VALIDATION_FAILED.getCode(), "请求参数格式错误")));
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常:", e);
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ResultCode.INTERNAL_SERVER_ERROR)));
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ResultCode.INTERNAL_SERVER_ERROR)));
    }
}
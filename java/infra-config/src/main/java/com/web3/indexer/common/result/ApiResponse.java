package com.web3.indexer.common.result;

import java.io.Serializable;

import com.web3.indexer.common.enums.ResultCode;

import lombok.Data;

/**
 * 统一API响应封装类
 *
 * @param <T> 数据类型
 */
@Data
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 响应码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    private Long timestamp;

    /**
     * 请求ID（用于链路追踪）
     */
    private String requestId;

    /**
     * 私有构造方法
     */
    private ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResponse<T> success() {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setCode(ResultCode.SUCCESS.getCode());
        response.setMessage(ResultCode.SUCCESS.getMessage());
        return response;
    }

    /**
     * 成功响应（有数据）
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setCode(ResultCode.SUCCESS.getCode());
        response.setMessage(ResultCode.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> ApiResponse<T> success(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setCode(ResultCode.SUCCESS.getCode());
        response.setMessage(message);
        return response;
    }

    /**
     * 成功响应（数据+消息）
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setCode(ResultCode.SUCCESS.getCode());
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    /**
     * 失败响应
     */
    public static <T> ApiResponse<T> error(String code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    /**
     * 失败响应（使用ResultCode）
     */
    public static <T> ApiResponse<T> error(ResultCode resultCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(resultCode.getCode());
        response.setMessage(resultCode.getMessage());
        return response;
    }

    /**
     * 失败响应（使用ResultCode + 自定义消息）
     */
    public static <T> ApiResponse<T> error(ResultCode resultCode, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(resultCode.getCode());
        response.setMessage(message);
        return response;
    }

    /**
     * 设置请求ID
     */
    public ApiResponse<T> requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }
}
package com.web3.indexer.common.enums;

/**
 * 响应码枚举
 */
public enum ResultCode {
    SUCCESS("200", "成功"),
    BAD_REQUEST("400", "请求参数错误"),
    UNAUTHORIZED("401", "未授权"),
    FORBIDDEN("403", "禁止访问"),
    NOT_FOUND("404", "资源未找到"),
    INTERNAL_SERVER_ERROR("500", "服务器内部错误"),
    SERVICE_UNAVAILABLE("503", "服务不可用"),

    PARAM_VALIDATION_FAILED("1001", "参数校验失败"),
    DATA_NOT_EXIST("2001", "数据不存在");

    private final String code;
    private final String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
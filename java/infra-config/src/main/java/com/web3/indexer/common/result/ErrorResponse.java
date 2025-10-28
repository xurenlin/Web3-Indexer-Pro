package com.web3.indexer.common.result;

import lombok.Data;

/**
 * 错误响应封装
 */
@Data
public class ErrorResponse {
    private String code;
    private String message;
    private String traceId;
    private String path;
    private Long timestamp;

    public ErrorResponse(String code, String message, String traceId, String path) {
        this.code = code;
        this.message = message;
        this.traceId = traceId;
        this.path = path;
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public static ErrorResponse of(String code, String message, String path) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(code);
        response.setMessage(message);
        response.setPath(path);
        return response;
    }

}

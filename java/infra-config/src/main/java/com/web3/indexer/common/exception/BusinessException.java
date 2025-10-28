package com.web3.indexer.common.exception;

import com.web3.indexer.common.enums.ResultCode;

/**
 * 业务异常基类
 */
public class BusinessException extends RuntimeException {

    private final String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }

    public String getCode() {
        return code;
    }
}

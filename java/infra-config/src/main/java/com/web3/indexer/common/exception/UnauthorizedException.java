package com.web3.indexer.common.exception;

import com.web3.indexer.common.enums.ResultCode;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(String message) {
        super(ResultCode.UNAUTHORIZED.getCode(), message);
    }

}

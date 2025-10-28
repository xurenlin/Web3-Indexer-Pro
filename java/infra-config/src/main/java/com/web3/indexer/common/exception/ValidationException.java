package com.web3.indexer.common.exception;

import com.web3.indexer.common.enums.ResultCode;

public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(ResultCode.PARAM_VALIDATION_FAILED.getCode(), message);
    }

}

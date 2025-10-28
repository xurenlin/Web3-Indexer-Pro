package com.web3.indexer.common.exception;

import com.web3.indexer.common.enums.ResultCode;

public class DataNotFoundException extends BusinessException {

    public DataNotFoundException(String message) {
        super(ResultCode.DATA_NOT_EXIST.getCode(), message);
    }

}

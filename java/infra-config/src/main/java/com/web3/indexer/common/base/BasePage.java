package com.web3.indexer.common.base;

import java.io.Serializable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BasePage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码，从1开始
     */
    @Min(value = 1, message = "页码最小为1")
    private int page = 1;

    /**
     * 每页数量
     */
    @Min(value = 1, message = "每页数量最小为1")
    @Max(value = 1000, message = "每页数量最大为1000")
    private int pageSize = 10;

}

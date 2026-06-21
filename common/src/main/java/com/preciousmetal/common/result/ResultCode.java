package com.preciousmetal.common.result;

public enum ResultCode {

    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(500, "系统异常"),
    PARAM_ERROR(400, "参数错误"),
    STOCK_NOT_FOUND(1001, "股票不存在"),
    MARKET_DATA_UNAVAILABLE(1002, "行情数据不可用"),
    RISK_CALCULATION_FAILED(2001, "风险计算失败"),
    INSUFFICIENT_DATA(2002, "数据不足，无法计算");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

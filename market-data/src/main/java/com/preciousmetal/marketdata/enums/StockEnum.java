package com.preciousmetal.marketdata.enums;

import java.math.BigDecimal;

public enum StockEnum {

    HUNAN_SILVER("600678", "湖南白银", StockType.SILVER, new BigDecimal("8.50")),
    ZHAOJIN_MINING("601899", "招金矿业", StockType.GOLD, new BigDecimal("12.30")),
    SHANDONG_GOLD("600547", "山东黄金", StockType.GOLD, new BigDecimal("25.80")),
    ZHONGJIN_GOLD("600489", "中金黄金", StockType.GOLD, new BigDecimal("10.15")),
    YUNNAN_SILVER("000603", "云南白银", StockType.SILVER, new BigDecimal("6.75")),
    CHIFENG_GOLD("600988", "赤峰黄金", StockType.GOLD, new BigDecimal("18.60"));

    private final String code;
    private final String name;
    private final StockType type;
    private final BigDecimal basePrice;

    StockEnum(String code, String name, StockType type, BigDecimal basePrice) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.basePrice = basePrice;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public StockType getType() {
        return type;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public enum StockType {
        GOLD, SILVER
    }

    public static StockEnum getByCode(String code) {
        for (StockEnum stock : values()) {
            if (stock.getCode().equals(code)) {
                return stock;
            }
        }
        return null;
    }
}

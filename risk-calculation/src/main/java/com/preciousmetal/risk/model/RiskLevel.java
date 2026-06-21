package com.preciousmetal.risk.model;

import java.math.BigDecimal;

public enum RiskLevel {

    LOW(0, 30, "低风险", "绿色"),
    MEDIUM(30, 50, "中等风险", "黄色"),
    HIGH(50, 70, "高风险", "橙色"),
    EXTREME(70, 100, "极端风险", "红色");

    private final double min;
    private final double max;
    private final String description;
    private final String color;

    RiskLevel(double min, double max, String description, String color) {
        this.min = min;
        this.max = max;
        this.description = description;
        this.color = color;
    }

    public static RiskLevel fromIndex(double index) {
        for (RiskLevel level : values()) {
            if (index >= level.min && index < level.max) {
                return level;
            }
        }
        return EXTREME;
    }

    public static RiskLevel fromIndex(BigDecimal index) {
        return fromIndex(index.doubleValue());
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }
}

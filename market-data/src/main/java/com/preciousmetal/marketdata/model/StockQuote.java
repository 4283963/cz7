package com.preciousmetal.marketdata.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StockQuote {

    private String stockCode;
    private String stockName;
    private String stockType;
    private BigDecimal price;
    private BigDecimal changePercent;
    private BigDecimal changeAmount;
    private Long volume;
    private BigDecimal turnover;
    private LocalDate tradeDate;

    public StockQuote() {
    }

    public StockQuote(String stockCode, String stockName, String stockType, BigDecimal price,
                      BigDecimal changePercent, BigDecimal changeAmount, Long volume,
                      BigDecimal turnover, LocalDate tradeDate) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.stockType = stockType;
        this.price = price;
        this.changePercent = changePercent;
        this.changeAmount = changeAmount;
        this.volume = volume;
        this.turnover = turnover;
        this.tradeDate = tradeDate;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(BigDecimal changePercent) {
        this.changePercent = changePercent;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String stockCode;
        private String stockName;
        private String stockType;
        private BigDecimal price;
        private BigDecimal changePercent;
        private BigDecimal changeAmount;
        private Long volume;
        private BigDecimal turnover;
        private LocalDate tradeDate;

        public Builder stockCode(String stockCode) {
            this.stockCode = stockCode;
            return this;
        }

        public Builder stockName(String stockName) {
            this.stockName = stockName;
            return this;
        }

        public Builder stockType(String stockType) {
            this.stockType = stockType;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder changePercent(BigDecimal changePercent) {
            this.changePercent = changePercent;
            return this;
        }

        public Builder changeAmount(BigDecimal changeAmount) {
            this.changeAmount = changeAmount;
            return this;
        }

        public Builder volume(Long volume) {
            this.volume = volume;
            return this;
        }

        public Builder turnover(BigDecimal turnover) {
            this.turnover = turnover;
            return this;
        }

        public Builder tradeDate(LocalDate tradeDate) {
            this.tradeDate = tradeDate;
            return this;
        }

        public StockQuote build() {
            return new StockQuote(stockCode, stockName, stockType, price, changePercent,
                    changeAmount, volume, turnover, tradeDate);
        }
    }
}

package com.preciousmetal.marketdata.model;

import java.util.List;

public class MarketDataSnapshot {

    private String snapshotId;
    private Long timestamp;
    private List<StockQuote> quotes;
    private Integer stockCount;

    public MarketDataSnapshot() {
    }

    public MarketDataSnapshot(String snapshotId, Long timestamp, List<StockQuote> quotes, Integer stockCount) {
        this.snapshotId = snapshotId;
        this.timestamp = timestamp;
        this.quotes = quotes;
        this.stockCount = stockCount;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<StockQuote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<StockQuote> quotes) {
        this.quotes = quotes;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String snapshotId;
        private Long timestamp;
        private List<StockQuote> quotes;
        private Integer stockCount;

        public Builder snapshotId(String snapshotId) {
            this.snapshotId = snapshotId;
            return this;
        }

        public Builder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder quotes(List<StockQuote> quotes) {
            this.quotes = quotes;
            return this;
        }

        public Builder stockCount(Integer stockCount) {
            this.stockCount = stockCount;
            return this;
        }

        public MarketDataSnapshot build() {
            return new MarketDataSnapshot(snapshotId, timestamp, quotes, stockCount);
        }
    }
}

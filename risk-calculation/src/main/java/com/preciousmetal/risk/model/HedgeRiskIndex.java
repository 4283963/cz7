package com.preciousmetal.risk.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HedgeRiskIndex {

    private String indexId;
    private BigDecimal riskIndex;
    private RiskLevel riskLevel;
    private BigDecimal goldAvgChange;
    private BigDecimal silverAvgChange;
    private BigDecimal changeDivergence;
    private BigDecimal goldVolumeRatio;
    private BigDecimal silverVolumeRatio;
    private BigDecimal volumeDivergence;
    private Integer goldStockCount;
    private Integer silverStockCount;
    private LocalDateTime calculatedAt;

    public HedgeRiskIndex() {
    }

    public HedgeRiskIndex(String indexId, BigDecimal riskIndex, RiskLevel riskLevel,
                          BigDecimal goldAvgChange, BigDecimal silverAvgChange,
                          BigDecimal changeDivergence, BigDecimal goldVolumeRatio,
                          BigDecimal silverVolumeRatio, BigDecimal volumeDivergence,
                          Integer goldStockCount, Integer silverStockCount, LocalDateTime calculatedAt) {
        this.indexId = indexId;
        this.riskIndex = riskIndex;
        this.riskLevel = riskLevel;
        this.goldAvgChange = goldAvgChange;
        this.silverAvgChange = silverAvgChange;
        this.changeDivergence = changeDivergence;
        this.goldVolumeRatio = goldVolumeRatio;
        this.silverVolumeRatio = silverVolumeRatio;
        this.volumeDivergence = volumeDivergence;
        this.goldStockCount = goldStockCount;
        this.silverStockCount = silverStockCount;
        this.calculatedAt = calculatedAt;
    }

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public BigDecimal getRiskIndex() {
        return riskIndex;
    }

    public void setRiskIndex(BigDecimal riskIndex) {
        this.riskIndex = riskIndex;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public BigDecimal getGoldAvgChange() {
        return goldAvgChange;
    }

    public void setGoldAvgChange(BigDecimal goldAvgChange) {
        this.goldAvgChange = goldAvgChange;
    }

    public BigDecimal getSilverAvgChange() {
        return silverAvgChange;
    }

    public void setSilverAvgChange(BigDecimal silverAvgChange) {
        this.silverAvgChange = silverAvgChange;
    }

    public BigDecimal getChangeDivergence() {
        return changeDivergence;
    }

    public void setChangeDivergence(BigDecimal changeDivergence) {
        this.changeDivergence = changeDivergence;
    }

    public BigDecimal getGoldVolumeRatio() {
        return goldVolumeRatio;
    }

    public void setGoldVolumeRatio(BigDecimal goldVolumeRatio) {
        this.goldVolumeRatio = goldVolumeRatio;
    }

    public BigDecimal getSilverVolumeRatio() {
        return silverVolumeRatio;
    }

    public void setSilverVolumeRatio(BigDecimal silverVolumeRatio) {
        this.silverVolumeRatio = silverVolumeRatio;
    }

    public BigDecimal getVolumeDivergence() {
        return volumeDivergence;
    }

    public void setVolumeDivergence(BigDecimal volumeDivergence) {
        this.volumeDivergence = volumeDivergence;
    }

    public Integer getGoldStockCount() {
        return goldStockCount;
    }

    public void setGoldStockCount(Integer goldStockCount) {
        this.goldStockCount = goldStockCount;
    }

    public Integer getSilverStockCount() {
        return silverStockCount;
    }

    public void setSilverStockCount(Integer silverStockCount) {
        this.silverStockCount = silverStockCount;
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String indexId;
        private BigDecimal riskIndex;
        private RiskLevel riskLevel;
        private BigDecimal goldAvgChange;
        private BigDecimal silverAvgChange;
        private BigDecimal changeDivergence;
        private BigDecimal goldVolumeRatio;
        private BigDecimal silverVolumeRatio;
        private BigDecimal volumeDivergence;
        private Integer goldStockCount;
        private Integer silverStockCount;
        private LocalDateTime calculatedAt;

        public Builder indexId(String indexId) {
            this.indexId = indexId;
            return this;
        }

        public Builder riskIndex(BigDecimal riskIndex) {
            this.riskIndex = riskIndex;
            return this;
        }

        public Builder riskLevel(RiskLevel riskLevel) {
            this.riskLevel = riskLevel;
            return this;
        }

        public Builder goldAvgChange(BigDecimal goldAvgChange) {
            this.goldAvgChange = goldAvgChange;
            return this;
        }

        public Builder silverAvgChange(BigDecimal silverAvgChange) {
            this.silverAvgChange = silverAvgChange;
            return this;
        }

        public Builder changeDivergence(BigDecimal changeDivergence) {
            this.changeDivergence = changeDivergence;
            return this;
        }

        public Builder goldVolumeRatio(BigDecimal goldVolumeRatio) {
            this.goldVolumeRatio = goldVolumeRatio;
            return this;
        }

        public Builder silverVolumeRatio(BigDecimal silverVolumeRatio) {
            this.silverVolumeRatio = silverVolumeRatio;
            return this;
        }

        public Builder volumeDivergence(BigDecimal volumeDivergence) {
            this.volumeDivergence = volumeDivergence;
            return this;
        }

        public Builder goldStockCount(Integer goldStockCount) {
            this.goldStockCount = goldStockCount;
            return this;
        }

        public Builder silverStockCount(Integer silverStockCount) {
            this.silverStockCount = silverStockCount;
            return this;
        }

        public Builder calculatedAt(LocalDateTime calculatedAt) {
            this.calculatedAt = calculatedAt;
            return this;
        }

        public HedgeRiskIndex build() {
            return new HedgeRiskIndex(indexId, riskIndex, riskLevel, goldAvgChange, silverAvgChange,
                    changeDivergence, goldVolumeRatio, silverVolumeRatio, volumeDivergence,
                    goldStockCount, silverStockCount, calculatedAt);
        }
    }
}

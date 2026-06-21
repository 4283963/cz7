package com.preciousmetal.risk.model;

import java.math.BigDecimal;
import java.util.List;

public class RiskAnalysisReport {

    private String reportId;
    private HedgeRiskIndex riskIndex;
    private List<String> riskFactors;
    private List<String> suggestions;
    private BigDecimal hedgeRatioRecommendation;
    private String summary;

    public RiskAnalysisReport() {
    }

    public RiskAnalysisReport(String reportId, HedgeRiskIndex riskIndex, List<String> riskFactors,
                              List<String> suggestions, BigDecimal hedgeRatioRecommendation, String summary) {
        this.reportId = reportId;
        this.riskIndex = riskIndex;
        this.riskFactors = riskFactors;
        this.suggestions = suggestions;
        this.hedgeRatioRecommendation = hedgeRatioRecommendation;
        this.summary = summary;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public HedgeRiskIndex getRiskIndex() {
        return riskIndex;
    }

    public void setRiskIndex(HedgeRiskIndex riskIndex) {
        this.riskIndex = riskIndex;
    }

    public List<String> getRiskFactors() {
        return riskFactors;
    }

    public void setRiskFactors(List<String> riskFactors) {
        this.riskFactors = riskFactors;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public BigDecimal getHedgeRatioRecommendation() {
        return hedgeRatioRecommendation;
    }

    public void setHedgeRatioRecommendation(BigDecimal hedgeRatioRecommendation) {
        this.hedgeRatioRecommendation = hedgeRatioRecommendation;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String reportId;
        private HedgeRiskIndex riskIndex;
        private List<String> riskFactors;
        private List<String> suggestions;
        private BigDecimal hedgeRatioRecommendation;
        private String summary;

        public Builder reportId(String reportId) {
            this.reportId = reportId;
            return this;
        }

        public Builder riskIndex(HedgeRiskIndex riskIndex) {
            this.riskIndex = riskIndex;
            return this;
        }

        public Builder riskFactors(List<String> riskFactors) {
            this.riskFactors = riskFactors;
            return this;
        }

        public Builder suggestions(List<String> suggestions) {
            this.suggestions = suggestions;
            return this;
        }

        public Builder hedgeRatioRecommendation(BigDecimal hedgeRatioRecommendation) {
            this.hedgeRatioRecommendation = hedgeRatioRecommendation;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public RiskAnalysisReport build() {
            return new RiskAnalysisReport(reportId, riskIndex, riskFactors,
                    suggestions, hedgeRatioRecommendation, summary);
        }
    }
}

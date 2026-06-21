package com.preciousmetal.risk.alert;

import com.preciousmetal.marketdata.model.StockQuote;
import com.preciousmetal.risk.model.HedgeRiskIndex;

import java.time.LocalDateTime;
import java.util.List;

public class AlertEvent {

    private String alertId;
    private AlertLevel alertLevel;
    private String alertTitle;
    private String alertMessage;
    private HedgeRiskIndex riskIndex;
    private List<StockQuote> worstStocks;
    private LocalDateTime createdAt;
    private boolean logged;
    private boolean emailed;
    private String emailFilePath;

    public enum AlertLevel {
        WARNING("警告", "⚠️"),
        DANGER("危险", "🚨"),
        EXTREME("极端", "🔥");

        private final String description;
        private final String emoji;

        AlertLevel(String description, String emoji) {
            this.description = description;
            this.emoji = emoji;
        }

        public String getDescription() {
            return description;
        }

        public String getEmoji() {
            return emoji;
        }
    }

    public AlertEvent() {
        this.createdAt = LocalDateTime.now();
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public AlertLevel getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(AlertLevel alertLevel) {
        this.alertLevel = alertLevel;
    }

    public String getAlertTitle() {
        return alertTitle;
    }

    public void setAlertTitle(String alertTitle) {
        this.alertTitle = alertTitle;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public HedgeRiskIndex getRiskIndex() {
        return riskIndex;
    }

    public void setRiskIndex(HedgeRiskIndex riskIndex) {
        this.riskIndex = riskIndex;
    }

    public List<StockQuote> getWorstStocks() {
        return worstStocks;
    }

    public void setWorstStocks(List<StockQuote> worstStocks) {
        this.worstStocks = worstStocks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean isEmailed() {
        return emailed;
    }

    public void setEmailed(boolean emailed) {
        this.emailed = emailed;
    }

    public String getEmailFilePath() {
        return emailFilePath;
    }

    public void setEmailFilePath(String emailFilePath) {
        this.emailFilePath = emailFilePath;
    }
}

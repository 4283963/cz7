package com.preciousmetal.risk.alert;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "hedge.alert")
public class AlertConfig {

    private BigDecimal safetyLine = new BigDecimal("50");
    private BigDecimal extremeLine = new BigDecimal("70");
    private Integer worstStockCount = 3;
    private boolean enableLogAlert = true;
    private boolean enableEmailAlert = true;
    private String emailRecipient = "trader@preciousmetal.com";
    private String emailSender = "alert@preciousmetal.com";
    private String emailStorePath = "logs/alerts/";

    public BigDecimal getSafetyLine() {
        return safetyLine;
    }

    public void setSafetyLine(BigDecimal safetyLine) {
        this.safetyLine = safetyLine;
    }

    public BigDecimal getExtremeLine() {
        return extremeLine;
    }

    public void setExtremeLine(BigDecimal extremeLine) {
        this.extremeLine = extremeLine;
    }

    public Integer getWorstStockCount() {
        return worstStockCount;
    }

    public void setWorstStockCount(Integer worstStockCount) {
        this.worstStockCount = worstStockCount;
    }

    public boolean isEnableLogAlert() {
        return enableLogAlert;
    }

    public void setEnableLogAlert(boolean enableLogAlert) {
        this.enableLogAlert = enableLogAlert;
    }

    public boolean isEnableEmailAlert() {
        return enableEmailAlert;
    }

    public void setEnableEmailAlert(boolean enableEmailAlert) {
        this.enableEmailAlert = enableEmailAlert;
    }

    public String getEmailRecipient() {
        return emailRecipient;
    }

    public void setEmailRecipient(String emailRecipient) {
        this.emailRecipient = emailRecipient;
    }

    public String getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(String emailSender) {
        this.emailSender = emailSender;
    }

    public String getEmailStorePath() {
        return emailStorePath;
    }

    public void setEmailStorePath(String emailStorePath) {
        this.emailStorePath = emailStorePath;
    }
}

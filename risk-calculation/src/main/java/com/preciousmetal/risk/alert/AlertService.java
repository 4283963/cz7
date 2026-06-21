package com.preciousmetal.risk.alert;

import com.preciousmetal.marketdata.model.StockQuote;
import com.preciousmetal.risk.model.HedgeRiskIndex;
import com.preciousmetal.risk.model.RiskLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class AlertService {

    private static final Logger log = LoggerFactory.getLogger(AlertService.class);

    private final AlertConfig alertConfig;
    private final NotificationService notificationService;
    private final List<AlertEvent> alertHistory = new CopyOnWriteArrayList<>();

    public AlertService(AlertConfig alertConfig, NotificationService notificationService) {
        this.alertConfig = alertConfig;
        this.notificationService = notificationService;
    }

    public AlertEvent checkAndTriggerAlert(HedgeRiskIndex riskIndex, List<StockQuote> allQuotes) {
        BigDecimal riskValue = riskIndex.getRiskIndex();
        BigDecimal safetyLine = alertConfig.getSafetyLine();
        BigDecimal extremeLine = alertConfig.getExtremeLine();

        log.debug("检查警报触发条件: 风险指数={}, 安全线={}, 极端线={}", riskValue, safetyLine, extremeLine);

        if (riskValue.compareTo(safetyLine) < 0) {
            log.debug("风险指数 {} 低于安全线 {}，无需触发警报", riskValue, safetyLine);
            return null;
        }

        AlertEvent.AlertLevel alertLevel;
        String alertTitle;

        if (riskValue.compareTo(extremeLine) >= 0) {
            alertLevel = AlertEvent.AlertLevel.EXTREME;
            alertTitle = "对冲风险指数突破极端警戒线！";
        } else {
            alertLevel = AlertEvent.AlertLevel.DANGER;
            alertTitle = "对冲风险指数超过安全线";
        }

        if (riskIndex.getRiskLevel() == RiskLevel.HIGH
                || riskIndex.getRiskLevel() == RiskLevel.EXTREME) {
            if (alertLevel == AlertEvent.AlertLevel.DANGER) {
                alertLevel = AlertEvent.AlertLevel.DANGER;
            }
        }

        List<StockQuote> worstStocks = pickWorstStocks(allQuotes, alertConfig.getWorstStockCount());
        String alertMessage = buildAlertMessage(riskIndex, worstStocks);

        AlertEvent alert = new AlertEvent();
        alert.setAlertId(UUID.randomUUID().toString());
        alert.setAlertLevel(alertLevel);
        alert.setAlertTitle(alertTitle);
        alert.setAlertMessage(alertMessage);
        alert.setRiskIndex(riskIndex);
        alert.setWorstStocks(worstStocks);

        notificationService.sendAlert(alert);

        alertHistory.add(0, alert);
        if (alertHistory.size() > 100) {
            alertHistory.remove(alertHistory.size() - 1);
        }

        log.warn("警报触发成功！级别={}, 风险指数={}, 最差股票数={}",
                alertLevel, riskValue, worstStocks.size());

        return alert;
    }

    private List<StockQuote> pickWorstStocks(List<StockQuote> allQuotes, int count) {
        if (allQuotes == null || allQuotes.isEmpty()) {
            return new ArrayList<>();
        }

        return allQuotes.stream()
                .sorted(Comparator.comparing(StockQuote::getChangePercent,
                        Comparator.nullsFirst(BigDecimal::compareTo)))
                .limit(count)
                .toList();
    }

    private String buildAlertMessage(HedgeRiskIndex riskIndex, List<StockQuote> worstStocks) {
        StringBuilder sb = new StringBuilder();
        sb.append("对冲风险指数达到 ").append(riskIndex.getRiskIndex()).append("，");
        sb.append("超过安全线 ").append(alertConfig.getSafetyLine()).append("。");

        BigDecimal changeDiv = riskIndex.getChangeDivergence();
        BigDecimal volumeDiv = riskIndex.getVolumeDivergence();
        BigDecimal goldChange = riskIndex.getGoldAvgChange();
        BigDecimal silverChange = riskIndex.getSilverAvgChange();

        List<String> factors = new ArrayList<>();
        if (changeDiv.compareTo(new BigDecimal("1")) > 0) {
            factors.add("金银涨跌幅背离 " + changeDiv + "%");
        }
        if (volumeDiv.compareTo(new BigDecimal("20")) > 0) {
            factors.add("成交量分布偏离 " + volumeDiv + "%");
        }
        if (goldChange.compareTo(BigDecimal.ZERO) > 0 && silverChange.compareTo(BigDecimal.ZERO) < 0) {
            factors.add("黄金板块过热（+" + goldChange + "%）而白银板块下跌（" + silverChange + "%）");
        }

        if (!factors.isEmpty()) {
            sb.append("主要原因：").append(String.join("；", factors)).append("。");
        }

        if (!worstStocks.isEmpty()) {
            sb.append("表现最差的股票：");
            sb.append(worstStocks.stream()
                    .map(s -> s.getStockName() + "(" + s.getStockCode() + "：" + formatPercent(s.getChangePercent()) + ")")
                    .reduce((a, b) -> a + "、" + b)
                    .orElse(""));
            sb.append("。");
        }

        return sb.toString();
    }

    private String formatPercent(BigDecimal percent) {
        if (percent == null) return "0.00%";
        String sign = percent.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
        return sign + percent.toPlainString() + "%";
    }

    public List<AlertEvent> getAlertHistory() {
        return new ArrayList<>(alertHistory);
    }

    public List<AlertEvent> getAlertHistoryByLevel(AlertEvent.AlertLevel level) {
        return alertHistory.stream()
                .filter(a -> a.getAlertLevel() == level)
                .toList();
    }

    public AlertConfig getAlertConfig() {
        return alertConfig;
    }

    public void updateSafetyLine(BigDecimal newValue) {
        if (newValue == null || newValue.compareTo(BigDecimal.ZERO) < 0
                || newValue.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("安全线必须在 0-100 之间");
        }
        alertConfig.setSafetyLine(newValue);
        log.info("安全线已更新为: {}", newValue);
    }

    public void updateExtremeLine(BigDecimal newValue) {
        if (newValue == null || newValue.compareTo(BigDecimal.ZERO) < 0
                || newValue.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("极端线必须在 0-100 之间");
        }
        alertConfig.setExtremeLine(newValue);
        log.info("极端线已更新为: {}", newValue);
    }

    public void setLogAlertEnabled(boolean enabled) {
        alertConfig.setEnableLogAlert(enabled);
        log.info("日志警报已{}", enabled ? "启用" : "禁用");
    }

    public void setEmailAlertEnabled(boolean enabled) {
        alertConfig.setEnableEmailAlert(enabled);
        log.info("邮件警报已{}", enabled ? "启用" : "禁用");
    }
}

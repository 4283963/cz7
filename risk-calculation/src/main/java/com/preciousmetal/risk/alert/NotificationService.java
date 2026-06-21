package com.preciousmetal.risk.alert;

import com.preciousmetal.marketdata.model.StockQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BG_RED = "\u001B[41m";
    private static final String ANSI_BG_YELLOW = "\u001B[43m";

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final AlertConfig alertConfig;
    private final EmailService emailService;

    public NotificationService(AlertConfig alertConfig, EmailService emailService) {
        this.alertConfig = alertConfig;
        this.emailService = emailService;
    }

    public void sendAlert(AlertEvent alert) {
        log.info("准备发送警报, ID={}, 级别={}", alert.getAlertId(), alert.getAlertLevel());

        if (alertConfig.isEnableLogAlert()) {
            printBoldLogAlert(alert);
            alert.setLogged(true);
        }

        if (alertConfig.isEnableEmailAlert()) {
            String filePath = emailService.saveAlertEmail(alert);
            alert.setEmailFilePath(filePath);
            alert.setEmailed(true);
        }
    }

    public void printBoldLogAlert(AlertEvent alert) {
        AlertEvent.AlertLevel level = alert.getAlertLevel();
        String colorCode = getColorCode(level);
        String bgColor = getBgColorCode(level);

        String separator = "═".repeat(80);

        System.out.println();
        System.out.println(bgColor + " " + ANSI_BOLD + " ".repeat(78) + ANSI_RESET);
        System.out.println(bgColor + " " + ANSI_BOLD + formatCenter(level.getEmoji() + "  " + level.getDescription() + "警报：" + alert.getAlertTitle(), 78) + ANSI_RESET);
        System.out.println(bgColor + " " + ANSI_BOLD + " ".repeat(78) + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_BOLD + "警报编号：" + ANSI_RESET + alert.getAlertId());
        System.out.println(ANSI_BOLD + "触发时间：" + ANSI_RESET + alert.getCreatedAt().format(DTF));
        System.out.println(ANSI_BOLD + "风险指数：" + ANSI_RESET + colorCode + ANSI_BOLD + alert.getRiskIndex().getRiskIndex() + ANSI_RESET + " / 100");
        System.out.println(ANSI_BOLD + "风险等级：" + ANSI_RESET + colorCode + ANSI_BOLD + alert.getRiskIndex().getRiskLevel() + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_BOLD + "警报原因：" + ANSI_RESET);
        System.out.println("  " + alert.getAlertMessage());
        System.out.println();

        System.out.println(ANSI_BOLD + "市场数据概览：" + ANSI_RESET);
        System.out.println("  黄金类平均涨跌幅：" + formatChange(alert.getRiskIndex().getGoldAvgChange()));
        System.out.println("  白银类平均涨跌幅：" + formatChange(alert.getRiskIndex().getSilverAvgChange()));
        System.out.println("  涨跌幅背离度：" + alert.getRiskIndex().getChangeDivergence() + "%");
        System.out.println("  成交量背离度：" + alert.getRiskIndex().getVolumeDivergence() + "%");
        System.out.println();

        System.out.println(ANSI_BOLD + "表现最差的 " + alert.getWorstStocks().size() + " 只股票：" + ANSI_RESET);
        System.out.println(colorCode + separator + ANSI_RESET);
        System.out.println(ANSI_BOLD + String.format("  %-8s %-15s %-8s %-12s %-14s", "代码", "名称", "类型", "涨跌幅", "成交量") + ANSI_RESET);
        System.out.println(colorCode + separator + ANSI_RESET);
        for (int i = 0; i < alert.getWorstStocks().size(); i++) {
            StockQuote s = alert.getWorstStocks().get(i);
            String rank = ANSI_BOLD + (i + 1) + "." + ANSI_RESET;
            System.out.println(String.format("%s %-8s %-15s %-8s %-12s %,14d",
                    rank,
                    s.getStockCode(),
                    s.getStockName(),
                    s.getStockType(),
                    formatChange(s.getChangePercent()),
                    s.getVolume()));
        }
        System.out.println(colorCode + separator + ANSI_RESET);
        System.out.println();

        if (alert.getEmailFilePath() != null) {
            System.out.println(ANSI_BOLD + "邮件已保存至：" + ANSI_RESET + alert.getEmailFilePath());
        }
        System.out.println();
    }

    private String formatCenter(String text, int width) {
        int pad = (width - text.length()) / 2;
        if (pad <= 0) return text;
        return " ".repeat(pad) + text + " ".repeat(width - pad - text.length());
    }

    private String formatChange(BigDecimal percent) {
        if (percent == null) return "0.00%";
        String sign = percent.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
        String color = percent.compareTo(BigDecimal.ZERO) >= 0 ? "\u001B[32m" : ANSI_RED;
        return color + ANSI_BOLD + sign + percent.toPlainString() + "%" + ANSI_RESET;
    }

    private String getColorCode(AlertEvent.AlertLevel level) {
        return switch (level) {
            case WARNING -> ANSI_YELLOW;
            case DANGER, EXTREME -> ANSI_RED;
        };
    }

    private String getBgColorCode(AlertEvent.AlertLevel level) {
        return switch (level) {
            case WARNING -> ANSI_BG_YELLOW;
            case DANGER, EXTREME -> ANSI_BG_RED;
        };
    }
}

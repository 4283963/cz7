package com.preciousmetal.risk.alert;

import com.preciousmetal.marketdata.model.StockQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_DTF = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final AlertConfig alertConfig;

    public EmailService(AlertConfig alertConfig) {
        this.alertConfig = alertConfig;
    }

    public String saveAlertEmail(AlertEvent alert) {
        try {
            String emailContent = generateEmailContent(alert);
            Path storePath = Paths.get(alertConfig.getEmailStorePath());
            Files.createDirectories(storePath);

            String fileName = "ALERT_" + alert.getAlertLevel().name() + "_"
                    + alert.getCreatedAt().format(FILE_DTF) + "_" + alert.getAlertId() + ".eml";
            Path filePath = storePath.resolve(fileName);

            Files.writeString(filePath, emailContent, StandardCharsets.UTF_8);
            log.info("警报邮件已保存: {}", filePath.toAbsolutePath());
            return filePath.toAbsolutePath().toString();
        } catch (IOException e) {
            log.error("保存警报邮件失败", e);
            return null;
        }
    }

    private String generateEmailContent(AlertEvent alert) {
        StringBuilder sb = new StringBuilder();

        sb.append("From: ").append(alertConfig.getEmailSender()).append("\r\n");
        sb.append("To: ").append(alertConfig.getEmailRecipient()).append("\r\n");
        sb.append("Subject: ").append("[").append(alert.getAlertLevel().getDescription()).append("] ")
                .append(alert.getAlertTitle()).append("\r\n");
        sb.append("Date: ").append(alert.getCreatedAt().format(DTF)).append("\r\n");
        sb.append("Message-ID: <").append(alert.getAlertId()).append("@preciousmetal.com>\r\n");
        sb.append("Content-Type: text/plain; charset=utf-8\r\n");
        sb.append("\r\n");

        sb.append("=".repeat(80)).append("\r\n");
        sb.append("  ").append(alert.getAlertLevel().getEmoji()).append("  ")
                .append(alert.getAlertLevel().getDescription()).append("警报通知").append("\r\n");
        sb.append("=".repeat(80)).append("\r\n");
        sb.append("\r\n");

        sb.append("【警报编号】").append(alert.getAlertId()).append("\r\n");
        sb.append("【触发时间】").append(alert.getCreatedAt().format(DTF)).append("\r\n");
        sb.append("【风险指数】").append(alert.getRiskIndex().getRiskIndex()).append(" / 100\r\n");
        sb.append("【风险等级】").append(alert.getRiskIndex().getRiskLevel()).append("\r\n");
        sb.append("\r\n");

        sb.append("【警报详情】").append("\r\n");
        sb.append("  ").append(alert.getAlertMessage()).append("\r\n");
        sb.append("\r\n");

        sb.append("【市场数据】").append("\r\n");
        sb.append("  黄金类股票数量: ").append(alert.getRiskIndex().getGoldStockCount()).append(" 只\r\n");
        sb.append("  白银类股票数量: ").append(alert.getRiskIndex().getSilverStockCount()).append(" 只\r\n");
        sb.append("  黄金类平均涨跌幅: ").append(formatPercent(alert.getRiskIndex().getGoldAvgChange())).append("\r\n");
        sb.append("  白银类平均涨跌幅: ").append(formatPercent(alert.getRiskIndex().getSilverAvgChange())).append("\r\n");
        sb.append("  涨跌幅背离度: ").append(alert.getRiskIndex().getChangeDivergence()).append("%\r\n");
        sb.append("  成交量背离度: ").append(alert.getRiskIndex().getVolumeDivergence()).append("%\r\n");
        sb.append("\r\n");

        sb.append("【表现最差的 ").append(alert.getWorstStocks().size()).append(" 只股票】").append("\r\n");
        sb.append("-".repeat(80)).append("\r\n");
        sb.append(String.format("  %-10s %-18s %-10s %-12s %-16s", "代码", "名称", "类型", "涨跌幅", "成交量")).append("\r\n");
        sb.append("-".repeat(80)).append("\r\n");
        for (int i = 0; i < alert.getWorstStocks().size(); i++) {
            StockQuote s = alert.getWorstStocks().get(i);
            sb.append(String.format("  %-10s %-18s %-10s %-12s %,16d",
                    s.getStockCode(),
                    s.getStockName(),
                    s.getStockType(),
                    formatPercent(s.getChangePercent()),
                    s.getVolume())).append("\r\n");
        }
        sb.append("-".repeat(80)).append("\r\n");
        sb.append("\r\n");

        sb.append("【建议措施】").append("\r\n");
        sb.append("  1. 立即关注表现最差的股票，评估是否需要止损").append("\r\n");
        sb.append("  2. 检查黄金、白银仓位比例，必要时进行再平衡").append("\r\n");
        sb.append("  3. 考虑增加对冲头寸以降低风险敞口").append("\r\n");
        sb.append("  4. 密切关注后续市场变化，设置更严格的止损点").append("\r\n");
        sb.append("\r\n");

        sb.append("=".repeat(80)).append("\r\n");
        sb.append("  本邮件由贵金属风险监控系统自动发送，请勿直接回复。").append("\r\n");
        sb.append("=".repeat(80)).append("\r\n");

        return sb.toString();
    }

    private String formatPercent(BigDecimal percent) {
        if (percent == null) return "0.00%";
        String sign = percent.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
        return sign + percent.toPlainString() + "%";
    }
}

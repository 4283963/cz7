package com.preciousmetal.risk.alert;

import com.preciousmetal.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alert")
public class AlertController {

    private static final Logger log = LoggerFactory.getLogger(AlertController.class);

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/config")
    public Result<Map<String, Object>> getAlertConfig() {
        log.info("查询警报配置");
        AlertConfig cfg = alertService.getAlertConfig();
        Map<String, Object> config = new HashMap<>();
        config.put("safetyLine", cfg.getSafetyLine());
        config.put("extremeLine", cfg.getExtremeLine());
        config.put("worstStockCount", cfg.getWorstStockCount());
        config.put("enableLogAlert", cfg.isEnableLogAlert());
        config.put("enableEmailAlert", cfg.isEnableEmailAlert());
        config.put("emailRecipient", cfg.getEmailRecipient());
        config.put("emailSender", cfg.getEmailSender());
        config.put("emailStorePath", cfg.getEmailStorePath());
        return Result.success(config);
    }

    @PostMapping("/config/safety-line")
    public Result<Map<String, Object>> updateSafetyLine(@RequestParam BigDecimal value) {
        log.info("更新安全线阈值: {}", value);
        alertService.updateSafetyLine(value);
        return getAlertConfig();
    }

    @PostMapping("/config/extreme-line")
    public Result<Map<String, Object>> updateExtremeLine(@RequestParam BigDecimal value) {
        log.info("更新极端线阈值: {}", value);
        alertService.updateExtremeLine(value);
        return getAlertConfig();
    }

    @PostMapping("/config/log-alert/{enabled}")
    public Result<Map<String, Object>> setLogAlertEnabled(@PathVariable boolean enabled) {
        log.info("{}日志警报", enabled ? "启用" : "禁用");
        alertService.setLogAlertEnabled(enabled);
        return getAlertConfig();
    }

    @PostMapping("/config/email-alert/{enabled}")
    public Result<Map<String, Object>> setEmailAlertEnabled(@PathVariable boolean enabled) {
        log.info("{}邮件警报", enabled ? "启用" : "禁用");
        alertService.setEmailAlertEnabled(enabled);
        return getAlertConfig();
    }

    @GetMapping("/history")
    public Result<List<AlertEvent>> getAlertHistory() {
        log.info("查询警报历史，共 {} 条", alertService.getAlertHistory().size());
        return Result.success(alertService.getAlertHistory());
    }

    @GetMapping("/history/level/{level}")
    public Result<List<AlertEvent>> getAlertHistoryByLevel(@PathVariable AlertEvent.AlertLevel level) {
        log.info("按级别查询警报历史: {}", level);
        return Result.success(alertService.getAlertHistoryByLevel(level));
    }

    @PostMapping("/test-trigger")
    public Result<AlertEvent> testTriggerAlert(@RequestParam(defaultValue = "80") BigDecimal riskValue) {
        log.info("手动测试触发警报，模拟风险指数: {}", riskValue);
        return Result.success(null);
    }
}

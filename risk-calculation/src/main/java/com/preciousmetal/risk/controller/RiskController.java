package com.preciousmetal.risk.controller;

import com.preciousmetal.common.result.Result;
import com.preciousmetal.risk.model.HedgeRiskIndex;
import com.preciousmetal.risk.model.RiskAnalysisReport;
import com.preciousmetal.risk.service.RiskCalculationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private static final Logger log = LoggerFactory.getLogger(RiskController.class);

    private final RiskCalculationService riskCalculationService;

    public RiskController(RiskCalculationService riskCalculationService) {
        this.riskCalculationService = riskCalculationService;
    }

    @GetMapping("/index")
    public Result<HedgeRiskIndex> getHedgeRiskIndex() {
        log.info("查询对冲风险指数");
        HedgeRiskIndex riskIndex = riskCalculationService.calculateHedgeRiskIndex();
        return Result.success(riskIndex);
    }

    @PostMapping("/calculate")
    public Result<HedgeRiskIndex> calculateHedgeRiskIndex() {
        log.info("计算对冲风险指数");
        HedgeRiskIndex riskIndex = riskCalculationService.calculateHedgeRiskIndex();
        return Result.success(riskIndex);
    }

    @GetMapping("/report")
    public Result<RiskAnalysisReport> getRiskReport() {
        log.info("获取风险分析报告");
        RiskAnalysisReport report = riskCalculationService.generateRiskReport();
        return Result.success(report);
    }

    @PostMapping("/report/generate")
    public Result<RiskAnalysisReport> generateRiskReport() {
        log.info("生成风险分析报告");
        RiskAnalysisReport report = riskCalculationService.generateRiskReport();
        return Result.success(report);
    }
}

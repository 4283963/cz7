package com.preciousmetal.risk.service;

import com.preciousmetal.common.exception.BusinessException;
import com.preciousmetal.common.result.ResultCode;
import com.preciousmetal.marketdata.enums.StockEnum;
import com.preciousmetal.marketdata.model.StockQuote;
import com.preciousmetal.marketdata.service.MarketDataService;
import com.preciousmetal.risk.model.HedgeRiskIndex;
import com.preciousmetal.risk.model.RiskAnalysisReport;
import com.preciousmetal.risk.model.RiskLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RiskCalculationService {

    private static final Logger log = LoggerFactory.getLogger(RiskCalculationService.class);

    private final MarketDataService marketDataService;

    public RiskCalculationService(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    public HedgeRiskIndex calculateHedgeRiskIndex() {
        log.info("开始计算对冲风险指数...");

        List<StockQuote> goldQuotes = marketDataService.getQuotesByType(StockEnum.StockType.GOLD);
        List<StockQuote> silverQuotes = marketDataService.getQuotesByType(StockEnum.StockType.SILVER);

        if (goldQuotes.isEmpty() || silverQuotes.isEmpty()) {
            throw new BusinessException(ResultCode.INSUFFICIENT_DATA, "黄金或白银类股票数据不足");
        }

        BigDecimal goldAvgChange = calculateAvgChangePercent(goldQuotes);
        BigDecimal silverAvgChange = calculateAvgChangePercent(silverQuotes);

        BigDecimal changeDivergence = goldAvgChange.subtract(silverAvgChange).abs();

        BigDecimal goldTotalVolume = sumVolume(goldQuotes);
        BigDecimal silverTotalVolume = sumVolume(silverQuotes);
        BigDecimal totalVolume = goldTotalVolume.add(silverTotalVolume);

        BigDecimal goldVolumeRatio = totalVolume.compareTo(BigDecimal.ZERO) > 0
                ? goldTotalVolume.divide(totalVolume, 4, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        BigDecimal silverVolumeRatio = totalVolume.compareTo(BigDecimal.ZERO) > 0
                ? silverTotalVolume.divide(totalVolume, 4, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal volumeDivergence = goldVolumeRatio.subtract(silverVolumeRatio).abs();

        BigDecimal riskIndex = computeRiskIndex(
                goldAvgChange, silverAvgChange,
                changeDivergence, volumeDivergence
        );

        RiskLevel riskLevel = RiskLevel.fromIndex(riskIndex);

        HedgeRiskIndex result = HedgeRiskIndex.builder()
                .indexId(UUID.randomUUID().toString())
                .riskIndex(riskIndex.setScale(2, RoundingMode.HALF_UP))
                .riskLevel(riskLevel)
                .goldAvgChange(goldAvgChange.setScale(2, RoundingMode.HALF_UP))
                .silverAvgChange(silverAvgChange.setScale(2, RoundingMode.HALF_UP))
                .changeDivergence(changeDivergence.setScale(2, RoundingMode.HALF_UP))
                .goldVolumeRatio(goldVolumeRatio.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP))
                .silverVolumeRatio(silverVolumeRatio.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP))
                .volumeDivergence(volumeDivergence.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP))
                .goldStockCount(goldQuotes.size())
                .silverStockCount(silverQuotes.size())
                .calculatedAt(LocalDateTime.now())
                .build();

        log.info("对冲风险指数计算完成: 指数={}, 等级={}", result.getRiskIndex(), riskLevel);
        return result;
    }

    public RiskAnalysisReport generateRiskReport() {
        HedgeRiskIndex riskIndex = calculateHedgeRiskIndex();

        List<String> riskFactors = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();

        BigDecimal changeDiv = riskIndex.getChangeDivergence();
        BigDecimal volumeDiv = riskIndex.getVolumeDivergence();

        if (changeDiv.compareTo(new BigDecimal("1")) > 0) {
            riskFactors.add("金银涨跌幅背离较大，偏离值: " + changeDiv + "%");
        }

        if (volumeDiv.compareTo(new BigDecimal("20")) > 0) {
            riskFactors.add("成交量分布不均衡，偏离度: " + volumeDiv + "%");
        }

        BigDecimal goldChange = riskIndex.getGoldAvgChange();
        BigDecimal silverChange = riskIndex.getSilverAvgChange();
        boolean goldHot = goldChange.compareTo(silverChange) > 0
                && goldChange.compareTo(BigDecimal.ZERO) > 0;
        boolean silverCold = silverChange.compareTo(goldChange) < 0
                || silverChange.compareTo(BigDecimal.ZERO) < 0;

        if (goldHot && silverCold) {
            riskFactors.add("黄金板块过热，白银板块关注度不足");
            suggestions.add("建议减持黄金类股票，增持白银类股票以平衡头寸");
            suggestions.add("可考虑增加白银空头对冲黄金多头风险");
        } else if (!goldHot && !silverCold) {
            riskFactors.add("白银板块表现强于黄金板块");
            suggestions.add("关注白银补涨行情，适度配置白银类股票");
        } else {
            suggestions.add("金银走势相对均衡，可维持现有对冲比例");
        }

        switch (riskIndex.getRiskLevel()) {
            case EXTREME:
                suggestions.add("风险极高，建议立即调整持仓结构，降低风险敞口");
                suggestions.add("考虑使用期权等衍生品进行额外对冲");
                break;
            case HIGH:
                suggestions.add("风险较高，建议密切关注市场变化，适时再平衡");
                break;
            case MEDIUM:
                suggestions.add("风险适中，保持关注，维持现有对冲策略");
                break;
            case LOW:
                suggestions.add("风险较低，可适度增加杠杆或扩大头寸");
                break;
        }

        BigDecimal hedgeRatio = calculateHedgeRatio(riskIndex);

        String summary = generateSummary(riskIndex, riskFactors);

        return RiskAnalysisReport.builder()
                .reportId("RPT-" + System.currentTimeMillis())
                .riskIndex(riskIndex)
                .riskFactors(riskFactors)
                .suggestions(suggestions)
                .hedgeRatioRecommendation(hedgeRatio.setScale(2, RoundingMode.HALF_UP))
                .summary(summary)
                .build();
    }

    private BigDecimal calculateHedgeRatio(HedgeRiskIndex riskIndex) {
        BigDecimal baseRatio = new BigDecimal("1.0");
        BigDecimal riskFactor = riskIndex.getRiskIndex()
                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal adjustment = riskFactor.multiply(new BigDecimal("0.5"));

        if (riskIndex.getGoldAvgChange().compareTo(riskIndex.getSilverAvgChange()) > 0) {
            return baseRatio.add(adjustment);
        } else {
            return baseRatio.subtract(adjustment);
        }
    }

    private String generateSummary(HedgeRiskIndex riskIndex, List<String> riskFactors) {
        StringBuilder sb = new StringBuilder();
        sb.append("当前对冲风险等级为").append(riskIndex.getRiskLevel().getDescription());
        sb.append("(").append(riskIndex.getRiskIndex()).append("点)。");
        if (!riskFactors.isEmpty()) {
            sb.append("主要风险因素包括：");
            sb.append(String.join("；", riskFactors));
            sb.append("。");
        }
        sb.append("黄金类股票平均涨跌幅").append(riskIndex.getGoldAvgChange()).append("%，");
        sb.append("白银类股票平均涨跌幅").append(riskIndex.getSilverAvgChange()).append("%。");
        return sb.toString();
    }

    private BigDecimal computeRiskIndex(BigDecimal goldChange, BigDecimal silverChange,
                                        BigDecimal changeDivergence, BigDecimal volumeDivergence) {
        BigDecimal changeRisk = changeDivergence.multiply(new BigDecimal("10"));
        BigDecimal volumeRisk = volumeDivergence.multiply(new BigDecimal("100"));

        BigDecimal directionRisk = BigDecimal.ZERO;
        if (goldChange.compareTo(BigDecimal.ZERO) > 0
                && silverChange.compareTo(BigDecimal.ZERO) < 0) {
            directionRisk = goldChange.abs().add(silverChange.abs()).multiply(new BigDecimal("5"));
        } else if (goldChange.compareTo(BigDecimal.ZERO) < 0
                && silverChange.compareTo(BigDecimal.ZERO) > 0) {
            directionRisk = goldChange.abs().add(silverChange.abs()).multiply(new BigDecimal("3"));
        }

        BigDecimal magnitudeRisk = goldChange.abs().add(silverChange.abs()).multiply(new BigDecimal("2"));

        BigDecimal totalRisk = changeRisk
                .add(volumeRisk)
                .add(directionRisk)
                .add(magnitudeRisk);

        if (totalRisk.compareTo(BigDecimal.ZERO) < 0) {
            totalRisk = BigDecimal.ZERO;
        }
        if (totalRisk.compareTo(new BigDecimal("100")) > 0) {
            totalRisk = new BigDecimal("100");
        }

        return totalRisk;
    }

    private BigDecimal calculateAvgChangePercent(List<StockQuote> quotes) {
        if (quotes == null || quotes.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal sum = quotes.stream()
                .map(StockQuote::getChangePercent)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(new BigDecimal(quotes.size()), 4, RoundingMode.HALF_UP);
    }

    private BigDecimal sumVolume(List<StockQuote> quotes) {
        if (quotes == null || quotes.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return quotes.stream()
                .map(q -> BigDecimal.valueOf(q.getVolume()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

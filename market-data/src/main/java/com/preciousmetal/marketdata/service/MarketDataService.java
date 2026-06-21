package com.preciousmetal.marketdata.service;

import com.preciousmetal.common.exception.BusinessException;
import com.preciousmetal.common.result.ResultCode;
import com.preciousmetal.marketdata.enums.StockEnum;
import com.preciousmetal.marketdata.model.MarketDataSnapshot;
import com.preciousmetal.marketdata.model.StockQuote;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MarketDataService {

    private static final Logger log = LoggerFactory.getLogger(MarketDataService.class);

    private final Map<String, StockQuote> quoteCache = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private final AtomicLong snapshotCounter = new AtomicLong(0);

    @PostConstruct
    public void init() {
        log.info("初始化行情数据服务...");
        for (StockEnum stock : StockEnum.values()) {
            StockQuote quote = buildInitialQuote(stock);
            quoteCache.put(stock.getCode(), quote);
        }
        log.info("行情数据初始化完成，共加载 {} 只股票", quoteCache.size());
    }

    public List<StockQuote> getAllQuotes() {
        if (quoteCache.isEmpty()) {
            throw new BusinessException(ResultCode.MARKET_DATA_UNAVAILABLE);
        }
        return new ArrayList<>(quoteCache.values());
    }

    public StockQuote getQuoteByCode(String stockCode) {
        StockQuote quote = quoteCache.get(stockCode);
        if (quote == null) {
            throw new BusinessException(ResultCode.STOCK_NOT_FOUND, "股票代码 " + stockCode + " 不存在");
        }
        return quote;
    }

    public List<StockQuote> getQuotesByType(StockEnum.StockType type) {
        return quoteCache.values().stream()
                .filter(q -> q.getStockType().equals(type.name()))
                .toList();
    }

    public MarketDataSnapshot generateSnapshot() {
        List<StockQuote> quotes = simulatePriceChanges();
        MarketDataSnapshot snapshot = MarketDataSnapshot.builder()
                .snapshotId("SNAP-" + System.currentTimeMillis() + "-" + snapshotCounter.incrementAndGet())
                .timestamp(System.currentTimeMillis())
                .quotes(quotes)
                .stockCount(quotes.size())
                .build();
        log.debug("生成行情快照: {}，包含 {} 只股票", snapshot.getSnapshotId(), snapshot.getStockCount());
        return snapshot;
    }

    public List<StockQuote> simulatePriceChanges() {
        List<StockQuote> updatedQuotes = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (StockEnum stock : StockEnum.values()) {
            StockQuote currentQuote = quoteCache.get(stock.getCode());
            if (currentQuote == null) {
                currentQuote = buildInitialQuote(stock);
            }

            BigDecimal changePercent = generateRandomChangePercent(stock);
            BigDecimal currentPrice = currentQuote.getPrice();
            BigDecimal changeAmount = currentPrice.multiply(changePercent)
                    .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            BigDecimal newPrice = currentPrice.add(changeAmount);

            if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
                newPrice = new BigDecimal("0.01");
            }

            Long baseVolume = 1000000L + random.nextInt(5000000);
            BigDecimal volumeMultiplier = BigDecimal.ONE.add(changePercent.abs()
                    .divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP));
            Long volume = new BigDecimal(baseVolume).multiply(volumeMultiplier).longValue();
            BigDecimal turnover = newPrice.multiply(new BigDecimal(volume));

            StockQuote newQuote = StockQuote.builder()
                    .stockCode(stock.getCode())
                    .stockName(stock.getName())
                    .stockType(stock.getType().name())
                    .price(newPrice.setScale(2, RoundingMode.HALF_UP))
                    .changePercent(changePercent.setScale(2, RoundingMode.HALF_UP))
                    .changeAmount(changeAmount.setScale(2, RoundingMode.HALF_UP))
                    .volume(volume)
                    .turnover(turnover.setScale(2, RoundingMode.HALF_UP))
                    .tradeDate(today)
                    .build();

            quoteCache.put(stock.getCode(), newQuote);
            updatedQuotes.add(newQuote);

            log.debug("股票 {} {} 涨跌幅 {}%, 成交量 {}",
                    stock.getCode(), stock.getName(), changePercent, volume);
        }

        return updatedQuotes;
    }

    private BigDecimal generateRandomChangePercent(StockEnum stock) {
        double base = random.nextGaussian() * 1.5;
        double trendBias = switch (stock.getType()) {
            case GOLD -> 0.2;
            case SILVER -> -0.1;
        };
        return BigDecimal.valueOf(base + trendBias);
    }

    private StockQuote buildInitialQuote(StockEnum stock) {
        return StockQuote.builder()
                .stockCode(stock.getCode())
                .stockName(stock.getName())
                .stockType(stock.getType().name())
                .price(stock.getBasePrice())
                .changePercent(BigDecimal.ZERO)
                .changeAmount(BigDecimal.ZERO)
                .volume(0L)
                .turnover(BigDecimal.ZERO)
                .tradeDate(LocalDate.now())
                .build();
    }

    public void resetMarketData() {
        quoteCache.clear();
        for (StockEnum stock : StockEnum.values()) {
            StockQuote quote = buildInitialQuote(stock);
            quoteCache.put(stock.getCode(), quote);
        }
        log.info("行情数据已重置");
    }
}

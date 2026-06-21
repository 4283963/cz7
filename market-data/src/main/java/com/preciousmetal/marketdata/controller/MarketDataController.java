package com.preciousmetal.marketdata.controller;

import com.preciousmetal.common.result.Result;
import com.preciousmetal.marketdata.enums.StockEnum;
import com.preciousmetal.marketdata.model.MarketDataSnapshot;
import com.preciousmetal.marketdata.model.StockQuote;
import com.preciousmetal.marketdata.service.MarketDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/market")
public class MarketDataController {

    private static final Logger log = LoggerFactory.getLogger(MarketDataController.class);

    private final MarketDataService marketDataService;

    public MarketDataController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @GetMapping("/quotes")
    public Result<List<StockQuote>> getAllQuotes() {
        log.info("查询所有股票行情");
        List<StockQuote> quotes = marketDataService.getAllQuotes();
        return Result.success(quotes);
    }

    @GetMapping("/quotes/{stockCode}")
    public Result<StockQuote> getQuoteByCode(@PathVariable String stockCode) {
        log.info("查询股票行情: {}", stockCode);
        StockQuote quote = marketDataService.getQuoteByCode(stockCode);
        return Result.success(quote);
    }

    @GetMapping("/quotes/type/{type}")
    public Result<List<StockQuote>> getQuotesByType(@PathVariable StockEnum.StockType type) {
        log.info("按类型查询股票行情: {}", type);
        List<StockQuote> quotes = marketDataService.getQuotesByType(type);
        return Result.success(quotes);
    }

    @PostMapping("/snapshot/generate")
    public Result<MarketDataSnapshot> generateSnapshot() {
        log.info("生成行情快照");
        MarketDataSnapshot snapshot = marketDataService.generateSnapshot();
        return Result.success(snapshot);
    }

    @PostMapping("/simulate")
    public Result<List<StockQuote>> simulateMarket(
            @RequestParam(defaultValue = "1") int rounds) {
        log.info("模拟行情波动, 轮数: {}", rounds);
        List<StockQuote> result = null;
        for (int i = 0; i < rounds; i++) {
            result = marketDataService.simulatePriceChanges();
        }
        return Result.success(result);
    }

    @PostMapping("/reset")
    public Result<Void> resetMarket() {
        log.info("重置行情数据");
        marketDataService.resetMarketData();
        return Result.success();
    }
}

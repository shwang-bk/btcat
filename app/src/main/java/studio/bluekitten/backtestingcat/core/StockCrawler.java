package studio.bluekitten.backtestingcat.core;

import android.util.Log;

import java.io.IOException;
import java.util.Calendar;

import studio.bluekitten.backtestingcat.exceptions.NullStockException;
import studio.bluekitten.backtestingcat.util.TestActivity;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

public class StockCrawler {
    private String stockMeta;
    private StockB stock;
    private Calendar from, to;

    public StockCrawler(String stockMeta, Calendar from){
        this.stockMeta = stockMeta;
        this.from = from;
    }

    public StockCrawler(String stockMeta, Calendar from, Calendar to){
        this.stockMeta = stockMeta;
        this.from = from;
        this.to = to;
    }

    public StockB crawlStockOnYahooFinance(){
        String[] stockInfo = stockMeta.split(" ");
        String stockId = stockInfo[0];
        String stockName = stockInfo[1];

        try {

            Stock stock = tryGetStockFromOriginId(stockId);
//            if (stock.getCurrency() == null)
//                stock = tryGetStockFromTWId(stockId);
//            if (stock.getCurrency() == null)
//                stock = tryGetStockFromTWOId(stockId);
            if (stock.getCurrency() == null)
                throw new NullStockException("Stock " + stockMeta + " is null in Yahoo Finance");

            if(to != null)
                this.stock = new StockB(stock, from, to, Interval.DAILY);
            else
                this.stock = new StockB(stock, from, Interval.DAILY);

            this.stock.setName(stockName);
        }
        catch (NullStockException e){
            Log.d(TestActivity.TEST_LOG, e.getMessage());
        }

        return stock;
    }

    private Stock tryGetStockFromOriginId(String stockId){
        Log.d(TestActivity.TEST_LOG, "Get Stock " + stockId);
        Stock stock = null;
        do {
            try {
                stock = YahooFinance.get(stockId);
            } catch (IOException e) {
                // Yahoo finance does not include this id, so return null;
            }
        } while(stock == null);
        return stock;
    }

    private Stock tryGetStockFromTWId(String stockId) {
        Log.d(TestActivity.TEST_LOG, "Get Stock " + stockId + ".TW");
        Stock stock = null;
        do {
            try {
                stock = YahooFinance.get(stockId + ".TW");

            } catch (IOException e) {
                // Yahoo finance does not include this id, so return null;
            }
        } while(stock == null);
        return stock;
    }

    private Stock tryGetStockFromTWOId(String stockId){
        Log.d(TestActivity.TEST_LOG, "Get Stock " + stockId + ".TWO");
        Stock stock = null;
        do {
            try {
                stock = YahooFinance.get(stockId + ".TWO");
            } catch (IOException e) {
                // Yahoo finance does not include this id, so return null;
            }
        } while(stock == null);
        return stock;
    }
}

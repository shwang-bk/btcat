package studio.bluekitten.backtestingcat;

import android.util.Log;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import studio.bluekitten.backtestingcat.core.Report;
import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void matrix1dTest() throws Exception {
        Matrix1d mA = new Matrix1d(new int[]{1,0,0,1,0,1,1,1});
        Matrix1d mB = new Matrix1d(new int[]{0,0,1,1,1,0,0,1});
        Matrix1d mC = new Matrix1d(new double[]{0,1,3,7,12,32,5,1.8});

        assertArrayEquals("a: ", mA.toIntegerArray(), new int[]{1,0,0,1,0,1,1,1});
        assertArrayEquals("b: ", mB.toIntegerArray(), new int[]{0,0,1,1,1,0,0,1});
        assertArrayEquals("!a: ", mA.not().toIntegerArray(), new int[]{0,1,1,0,1,0,0,0});
        assertArrayEquals("a + b: ", mA.add(mB).toDoubleArray(), new double[]{1,0,1,2,1,1,1,2}, 0);
        assertArrayEquals("a - b: ", mA.subtract(mB).toDoubleArray(), new double[]{1,0,-1,0,-1,1,1,0}, 0);
        assertArrayEquals("a * b: ", mA.multiply(mB).toDoubleArray(), new double[]{0,0,0,1,0,0,0,1}, 0);
        assertArrayEquals("a / b: ", mA.divide(mB).toDoubleArray(), new double[]{0,0,0,1,0,0,0,1}, 0);

        assertArrayEquals("a & b: ", mA.and(mB).toIntegerArray(), new int[]{0,0,0,1,0,0,0,1});
        assertArrayEquals("a | b: ", mA.or(mB).toIntegerArray(), new int[]{1,0,1,1,1,1,1,1});
        assertArrayEquals("a > b: ", mA.moreThan(mB).toIntegerArray(), new int[]{1,0,0,0,0,1,1,0});
        assertArrayEquals("a < b: ", mA.lessThan(mB).toIntegerArray(), new int[]{0,0,1,0,1,0,0,0});
        assertArrayEquals("a = b: ", mA.equalTo(mB).toIntegerArray(), new int[]{0,1,0,1,0,0,0,1});

        assertEquals("c.sum(): ", mC.sum().floatValue(), (float)61.8, 0);
        assertEquals("c.max(): ", mC.max().floatValue(), (float)32, 0);
        assertEquals("c.min(): ", mC.min().floatValue(), (float)0, 0);
        assertEquals("c.average(): ", mC.mean().floatValue(), (float)7.725, 0);
        assertEquals("c.std(): ", mC.std().floatValue(), (float)9.860496, 0);
        assertArrayEquals("c.cumsum(): ", mC.cumsum().toFloatArray(), new float[]{0,1,4,11,23,55,60,(float)61.8},0);
        assertArrayEquals("c.shift(): ", mC.shift(1).toFloatArray(), new float[]{Float.NaN,0,1,3,7,12,32,5},0);

        assertArrayEquals("a > -1: ", mA.moreThan(-1).toIntegerArray(), new int[]{1,1,1,1,1,1,1,1});
        assertArrayEquals("a < 1: ", mA.lessThan(1).toIntegerArray(), new int[]{0,1,1,0,1,0,0,0});
    }

    @Test
    public void reportTest() {
        Report report = new Report();

        report.setChartLine("Line1");
        report.setProfit(20000);
        report.setGrossProfit(80000);
        report.setGrossLoss(60000);
        report.setTradeCount(25);
        report.setProfitCount(10);
        report.setLossCount(15);
        report.setMaxContinueProfit(3);
        report.setMaxContinueLoss(5);
        report.setMaxContinueLossTrade(20000);
        report.setNote("測試一");

        assertEquals("淨利: ", report.getProfit(), 20000);
        assertEquals("毛利: ", report.getGrossProfit(), 80000);
        assertEquals("毛損: ", report.getGrossLoss(), 60000);
        assertEquals("獲利因子: ", report.getProfitFactor(), 1.33, 0);

        assertEquals("總交易次數: ", report.getTradeCount(), 25);
        assertEquals("獲利次數: ", report.getProfitCount(), 10);
        assertEquals("虧損次數: ", report.getLossCount(), 15);
        assertEquals("勝率: ", report.getPercentProfitable(), 40, 0);

        assertEquals("平均獲利: ", report.getAverageProfit(), 8000, 0);
        assertEquals("平均虧損: ", report.getAverageLoss(), 4000, 0);
        assertEquals("賺賠比: ", report.getProfitRatio(), 2, 0);

        assertEquals("最大連續獲利次數: ", report.getMaxContinueProfit(), 3);
        assertEquals("最大連續虧損次數: ", report.getMaxContinueLoss(), 5);
        assertEquals("最大策略虧損: ", report.getMaxContinueLossTrade(), 20000);
        assertEquals("報酬率: ", report.getPercentTrading(), 100, 0);
    }

    @Test
    public void yahooFinanceAPITest() {
        Calendar testDate = Calendar.getInstance();
        Calendar testNextDate = Calendar.getInstance();
        testDate.set(2017, 5, 1);
        testNextDate.set(2017, 5, 2);

        Stock stock = null;
        do {
            try {
                stock = YahooFinance.get("2330.TW");

            } catch (IOException e) {
                // Yahoo finance does not include this id, so return null;
            }
        } while(stock == null);
        List<HistoricalQuote> quotes = new ArrayList<>();
        try {
            quotes = stock.getHistory(testDate, testNextDate, Interval.DAILY);
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (HistoricalQuote historicalQuote : quotes) {
            System.out.println(historicalQuote.getLow());
        }

        assertEquals("交易筆數:", quotes.size(), 1);
        assertEquals("開盤:", quotes.get(0).getOpen().doubleValue(), 208.5, 0);
        assertEquals("最高:", quotes.get(0).getHigh().doubleValue(), 209, 0);
        assertEquals("最低:", quotes.get(0).getLow().doubleValue(), 207.5, 0);
        assertEquals("收盤:", quotes.get(0).getClose().doubleValue(), 209, 0);
    }

    @Test
    public void stockBTest() {
        Calendar testDate = Calendar.getInstance();
        Calendar testNextDate = Calendar.getInstance();
        testDate.set(2017, 5, 1);
        testNextDate.set(2017, 5, 2);

        Stock stock = null;
        do {
            try {
                stock = YahooFinance.get("2330.TW");

            } catch (IOException e) {
                // Yahoo finance does not include this id, so return null;
            }
        } while(stock == null);

        StockB stockB = new StockB(stock, testDate, testNextDate, Interval.DAILY);

        Matrix1d opens = stockB.getOpens();
        Matrix1d highs = stockB.getHighs();
        Matrix1d lows = stockB.getLows();
        Matrix1d closes = stockB.getCloses();

        assertEquals("開盤:", opens.get(0).doubleValue(), 208.5, 0);
        assertEquals("最高:", highs.get(0).doubleValue(), 209, 0);
        assertEquals("最低:", lows.get(0).doubleValue(), 207.5, 0);
        assertEquals("收盤:", closes.get(0).doubleValue(), 209, 0);
    }
}
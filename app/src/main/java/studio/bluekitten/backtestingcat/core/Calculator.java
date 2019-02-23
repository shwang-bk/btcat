package studio.bluekitten.backtestingcat.core;

import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import studio.bluekitten.backtestingcat.core.strategies.Strategy;
import studio.bluekitten.backtestingcat.util.Matrix1d;
import studio.bluekitten.backtestingcat.util.TestActivity;

public class Calculator {

    private List<Strategy> inStrategys, outStrategys;
    private String lines, notes, timingSignals, targetName, timestamp;
    private double yearsOffset;

    private int stopLoss = 1000,               //停損點
                stopProfit = 10000,             //停利點
                profit,                 // 淨利
                grossProfit,            // 毛利
                grossLoss,              // 毛損
                tradeCount,             // 總交易次數
                profitCount,            // 獲利次數
                lossCount,              // 虧損次數
                maxContinueProfit,      // 最大連續獲利次數
                maxContinueLoss,        // 最大連續虧損次數
                maxContinueLossTrade;   // 最大策略虧損

    public Calculator(List<Strategy> inStrategys,
                      List<Strategy> outStrategys){
        this.inStrategys = inStrategys;
        this.outStrategys = outStrategys;
    }

    public void setStopLoss(int stopLoss) {
        this.stopLoss = stopLoss;
    }

    public void setStopProfit(int stopProfit) {
        this.stopProfit = stopProfit;
    }

    public Report calculate(List<StockB> stocks){

        int days = stocks.get(0).getCloses().size();
        Matrix1d gainOrLoss = new Matrix1d(0, days);

        for(StockB stock : stocks){
            targetName = getTargetName(stock);
            yearsOffset = stock.getYearsOffset();
            timestamp = stock.getTimestamp();

            // 歷史收盤價
            Matrix1d closes = stock.getCloses();

            // 實際漲跌價格
            Matrix1d quoteChange = closes.subtract(closes.shift(1));

            // 取得進出場訊號
            Matrix1d inSignals = getMergeSignals(stock, inStrategys, days);
            Matrix1d outSignals = getMergeSignals(stock, outStrategys, days);

            // 取得持有訊號
            Matrix1d holdSignals = getHoldSignals(inSignals, outSignals);
            holdSignals = fixHoldSignals(holdSignals, closes, stopLoss, stopProfit);
            timingSignals = getTimingSignals(holdSignals).toString();

            // 實現損益(線圖用)
            Matrix1d partGainOrLoss = quoteChange
                .multiply(holdSignals)
                .multiply(new Matrix1d(1000, days));
            gainOrLoss = gainOrLoss.add(partGainOrLoss);

            // 取得損益紀錄
            Matrix1d realizeGainOrLossRecord = getRealizeGainOrLossRecord(holdSignals, quoteChange);
            Matrix1d realizeGainRecord = realizeGainOrLossRecord.moreThan(0).multiply(realizeGainOrLossRecord);
            Matrix1d realizeLossRecord = realizeGainOrLossRecord.lessThan(0).multiply(realizeGainOrLossRecord);

            //生成報告用的各項數值
            tradeCount += realizeGainOrLossRecord.size();
            profitCount += realizeGainRecord.nonZeroSize();
            lossCount += realizeLossRecord.nonZeroSize();

            maxContinueProfit = getMaxContinueCount(realizeGainRecord, maxContinueProfit);
            maxContinueLoss = getMaxContinueCount(realizeLossRecord, maxContinueLoss);
        }

        profit = gainOrLoss.sum().intValue();
        grossProfit = gainOrLoss.moreThan(0)
                            .multiply(gainOrLoss)
                            .sum().intValue();

        grossLoss = -(gainOrLoss.lessThan(0)
                            .multiply(gainOrLoss)
                            .sum().intValue());

        maxContinueLossTrade = getMaxContinueLossTrade(gainOrLoss);
        lines = gainOrLoss.cumsum().toString();
        notes = getNotes(stocks);

        return generateReport();
    }

    // 取得策略整合訊號
    private Matrix1d getMergeSignals(StockB stock, List<Strategy> strategys, int size){
        if(strategys.size() == 0){
            return new Matrix1d(0, size);
        }
        Matrix1d signals = new Matrix1d(1, size);
        for(Strategy strategy : strategys)
            signals = signals.and(strategy.getSignals(stock));

        return signals;

    }

    // 取得持有訊號
    private Matrix1d getHoldSignals(Matrix1d in, Matrix1d out){

        // 進出場訊號: 1 進場, -1 出場
        int[] inout_signals = in.subtract(out).toIntegerArray();

        // 持有訊號: 1 持有, 0 未持有
        int[] hold_signals = new int[inout_signals.length];
        int hold = 0;
        for(int i = 0; i < hold_signals.length; i++){
            int signal = hold + inout_signals[i];
            if(signal <= 1 && signal >= 0)
                hold = signal;
            hold_signals[i] = hold;
        }

        return new Matrix1d(hold_signals).shift(1);
    }

    private Matrix1d fixHoldSignals(Matrix1d holdSignals, Matrix1d closes, int stopLoss, int stopProfit){
        int[] holdArray = holdSignals.toIntegerArray();
        BigDecimal stopLossRate = new BigDecimal((100.0-stopLoss)/100.0);
        BigDecimal stopProfitRate = new BigDecimal((100.0+stopProfit)/100.0);

        double stopTopPrice = -1;
        double stopBottomPrice = -1;

        for(int i = 0; i < holdArray.length; i++){
            double currentPrice = closes.get(i).doubleValue();
            if(holdArray[i] == 1){
                if(stopTopPrice == -1) {
                    stopTopPrice = closes.get(i - 1).multiply(stopProfitRate).doubleValue();
                    stopBottomPrice = closes.get(i - 1).multiply(stopLossRate).doubleValue();
                }
                else if(currentPrice < stopBottomPrice || currentPrice > stopTopPrice){
                    if(i + 1 < holdArray.length)
                        holdArray[i + 1] = 0;

                    if(i + 2 < holdArray.length)
                        holdArray[i + 2] = 0;
                }
            }
            else{
                stopTopPrice = -1;
                stopBottomPrice = -1;
            }

        }

        return new Matrix1d(holdArray);
    }

    private Matrix1d getTimingSignals(Matrix1d hold){
        return new Matrix1d(
                hold.shift(-1)
                .subtract(hold)
                .toIntegerArray()
        );
    }

    // 取得損益紀錄
    private Matrix1d getRealizeGainOrLossRecord(Matrix1d holdSignals, Matrix1d quoteChanges){
        int[] holdArray = holdSignals.toIntegerArray();

        boolean isHold = false;
        BigDecimal gainOrLoss = BigDecimal.ZERO;
        List<BigDecimal> realizeGainOrLossList = new ArrayList<>();
        for(int i = 0; i < holdArray.length; i++){
            if(holdArray[i] == 1) {
                isHold = true;
                gainOrLoss = gainOrLoss.add(quoteChanges.get(i));
            }
            else if(isHold){
                isHold = false;
                realizeGainOrLossList.add(gainOrLoss);
                gainOrLoss = BigDecimal.ZERO;
            }
        }
        if(isHold)
            realizeGainOrLossList.add(gainOrLoss);

        return new Matrix1d(realizeGainOrLossList.toArray(new BigDecimal[realizeGainOrLossList.size()]));
    }

    // 取得最大連續交易紀錄
    private int getMaxContinueCount(Matrix1d record, int maxCount){
        int current = 0;
        int[] recordi = record.toIntegerArray();

        for(int r : recordi){
            if(r != 0)
                current++;
            else
                current = 0;

            maxCount = maxCount > current ? maxCount : current;
        }
        return maxCount;
    }

    // 取得最大連續虧損金額
    private int getMaxContinueLossTrade(Matrix1d record){
        Matrix1d lossRecord = record.lessThan(0).multiply(record);
        int min = 0, currentLoss = 0;

        for (int i : lossRecord.toIntegerArray()){
            if(i == 0)
                currentLoss = 0;

            else
                currentLoss += i;

            min = min < currentLoss ? min : currentLoss;
        }

        return -min;
    }

    //取得備註
    private String getNotes(List<StockB> stocks){

        String stockNames = "標的: ";
        String in = "進場條件: ";
        String out = "出場條件: ";

        for(StockB stock : stocks) {
            stockNames += "\t" + getTargetName(stock) + ",";
        }

        for(Strategy strategy : inStrategys)
            in += "\t" + strategy.descriptions() + ",";

        for(Strategy strategy : outStrategys)
            out += "\t" + strategy.descriptions() + ",";

        return stockNames + "\n" + in + "\n" + out;
    }

    private String getTargetName(StockB stock){
        return  "" + stock.getSymbol().split("\\.")[0]
                + " " + stock.getName();
    }

    // 生成報告
    private Report generateReport(){
        Report report = new Report();

        report.setChartLine(lines);
        report.setTargetName(targetName);
        report.setYearsOffset(yearsOffset);
        report.setTimingSignals(timingSignals);
        report.setProfit(profit);
        report.setGrossProfit(grossProfit);
        report.setGrossLoss(grossLoss);
        report.setTradeCount(tradeCount);
        report.setProfitCount(profitCount);
        report.setLossCount(lossCount);
        report.setMaxContinueProfit(maxContinueProfit);
        report.setMaxContinueLoss(maxContinueLoss);
        report.setMaxContinueLossTrade(maxContinueLossTrade);
        report.setTimestamp(timestamp);
        report.setNote(notes);

        return report;
    }
}

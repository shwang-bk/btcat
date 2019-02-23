package studio.bluekitten.backtestingcat.core;

import java.math.BigDecimal;

public class Report {

    public static final String FLAG_REPORT_ID = "reportId";

    private double  yearsOffset,
                    profitFactor,       // 獲利因子
                    percentProfitable,  // 勝率
                    averageProfit,      // 平均獲利
                    averageLoss,        // 平均虧損
                    profitRatio,        // 賺賠比
                    percentTrading;     // 報酬率

    private long id;

    private int profit,                 // 淨利
                grossProfit,            // 毛利
                grossLoss,              // 毛損
                tradeCount,             // 總交易次數
                profitCount,            // 獲利次數
                lossCount,              // 虧損次數
                maxContinueProfit,      // 最大連續獲利次數
                maxContinueLoss,        // 最大連續虧損次數
                maxContinueLossTrade;   // 最大策略虧損

    private String chartLine,
                   note,
                   timingSignals,
                   targetName,
                   timestamp;

    @Override
    public String toString(){
        String str = "";
        str += "targetName: " + targetName + "\n";
        str += "yearsOffset: " + yearsOffset + "\n";
        str += "profit: " + profit + "\n";
        str += "grossProfit: " + grossProfit + "\n";
        str += "grossLoss: " + grossLoss + "\n";
        str += "tradeCount: " + tradeCount + "\n";
        str += "profitCount: " + profitCount + "\n";
        str += "lossCount: " + lossCount + "\n";
        str += "maxContinueProfit: " + maxContinueProfit + "\n";
        str += "maxContinueLoss: " + maxContinueLoss + "\n";
        str += "maxContinueLossTrade: " + maxContinueLossTrade + "\n";
        str += "note: " + note + "\n";
        str += "timestamp: " + timestamp;
        return str;
    }

    public String getChartLine() {
        return chartLine;
    }

    public double getProfitFactor() {
        if(profitFactor != 0)
            return profitFactor;

        try{
            BigDecimal grossProfit = new BigDecimal(this.grossProfit);
            BigDecimal grossLoss = new BigDecimal(this.grossLoss);
            BigDecimal result = grossProfit.divide(grossLoss, 2, BigDecimal.ROUND_HALF_UP);
            profitFactor = result.doubleValue();
            return profitFactor;
        } catch (ArithmeticException ae){
            return 0;
        }
    }

    public double getPercentProfitable() {
        if(percentProfitable != 0)
            return percentProfitable;

        try{
            BigDecimal profitCount = new BigDecimal(this.profitCount);
            BigDecimal tradeCount = new BigDecimal(this.tradeCount);
            BigDecimal result = profitCount.divide(tradeCount, 3, BigDecimal.ROUND_HALF_UP)
                                    .multiply(new BigDecimal("100"));
            percentProfitable = result.doubleValue();
            return percentProfitable;
        } catch (ArithmeticException ae){
            return 0;
        }
    }

    public double getAverageProfit() {
        if(averageProfit != 0)
            return averageProfit;

        try{
            BigDecimal grossProfit = new BigDecimal(this.grossProfit);
            BigDecimal profitCount = new BigDecimal(this.profitCount);
            BigDecimal result = grossProfit.divide(profitCount, 2, BigDecimal.ROUND_HALF_UP);
            averageProfit = result.doubleValue();
            return averageProfit;
        } catch (ArithmeticException ae){
            return 0;
        }
    }

    public double getAverageLoss() {
        if(averageLoss != 0)
            return averageLoss;

        try{
            BigDecimal grossLoss = new BigDecimal(this.grossLoss);
            BigDecimal lossCount = new BigDecimal(this.lossCount);
            BigDecimal result = grossLoss.divide(lossCount, 2, BigDecimal.ROUND_HALF_UP);
            averageLoss = result.doubleValue();
            return averageLoss;
        } catch (ArithmeticException ae){
            return 0;
        }
    }

    public double getProfitRatio() {
        if(profitRatio != 0)
            return profitRatio;

        try{
            BigDecimal averageProfit = new BigDecimal(getAverageProfit());
            BigDecimal averageLoss = new BigDecimal(getAverageLoss());
            BigDecimal result = averageProfit.divide(averageLoss, 2, BigDecimal.ROUND_HALF_UP);
            profitRatio = result.doubleValue();
            return profitRatio;
        } catch (ArithmeticException ae){
            return 0;
        }
    }

    public double getPercentTrading() {
        if(percentTrading != 0)
            return percentTrading;

        try{
            BigDecimal profit = new BigDecimal(this.profit);
            BigDecimal maxContinueLossTrade = new BigDecimal(this.maxContinueLossTrade);
            BigDecimal result = profit.divide(maxContinueLossTrade, 2, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal("100"));
            percentTrading = result.doubleValue();
            return percentTrading;
        } catch (ArithmeticException ae){
            return 0;
        }
    }

    public int getProfit() {
        return profit;
    }

    public int getGrossProfit() {
        return grossProfit;
    }

    public int getGrossLoss() {
        return grossLoss;
    }

    public int getTradeCount() {
        return tradeCount;
    }

    public int getProfitCount() {
        return profitCount;
    }

    public int getLossCount() {
        return lossCount;
    }

    public int getMaxContinueProfit() {
        return maxContinueProfit;
    }

    public int getMaxContinueLoss() {
        return maxContinueLoss;
    }

    public int getMaxContinueLossTrade() {
        return maxContinueLossTrade;
    }

    public String getNote() {
        return note;
    }

    public String getTimingSignals() {
        return timingSignals;
    }

    public double getYearsOffset() {
        return yearsOffset;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public void setYearsOffset(double yearsOffset) {
        this.yearsOffset = yearsOffset;
    }

    public void setTimingSignals(String timingSignals) {
        this.timingSignals = timingSignals;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public void setGrossProfit(int grossProfit) {
        this.grossProfit = grossProfit;
    }

    public void setGrossLoss(int grossLoss) {
        this.grossLoss = grossLoss;
    }

    public void setTradeCount(int tradeCount) {
        this.tradeCount = tradeCount;
    }

    public void setProfitCount(int profitCount) {
        this.profitCount = profitCount;
    }

    public void setLossCount(int lossCount) {
        this.lossCount = lossCount;
    }

    public void setMaxContinueProfit(int maxContinueProfit) {
        this.maxContinueProfit = maxContinueProfit;
    }

    public void setMaxContinueLoss(int maxContinueLoss) {
        this.maxContinueLoss = maxContinueLoss;
    }

    public void setMaxContinueLossTrade(int maxContinueLossTrade) {
        this.maxContinueLossTrade = maxContinueLossTrade;
    }

    public void setChartLine(String chartLine) {
        this.chartLine = chartLine;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

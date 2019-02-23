package studio.bluekitten.backtestingcat.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import studio.bluekitten.backtestingcat.util.CalendarConverter;
import studio.bluekitten.backtestingcat.util.Matrix1d;
import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class StockB extends Stock {
    public static final int K = 0;
    public static final int D = 1;

    public static final int DIF = 0;
    public static final int DEM = 1;
    public static final int OSC = 2;

    public static final int BBAND_TOP_LINE = 0;
    public static final int BBAND_CENTER_LINE = 1;
    public static final int BBAND_BOTTOM_LINE = 2;

    public static final int CBAND_LINE_ONE = 0;
    public static final int CBAND_LINE_TWO = 1;
    public static final int CBAND_LINE_THREE = 2;
    public static final int CBAND_LINE_FOUR = 3;
    public static final int CBAND_LINE_FIVE = 4;

    private double yearsOffset;
    private Calendar[] calendarDates;
    private String name, timestamp;
    private List<HistoricalQuote> historicalQuotes = new ArrayList<>();
    private Matrix1d opens, closes, adjCloses, highs, lows, volumes;

    public StockB(Stock stock, Calendar from, Interval interval) {
        super(stock.getSymbol());
        initialStockInfo(stock);

        Calendar today = Calendar.getInstance();
        timestamp = CalendarConverter.toString(today);
        yearsOffset = getYears(from, today);

        try {
            List<HistoricalQuote> quotes = stock.getHistory(from, interval);
            transformQuotes(quotes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StockB(Stock stock, Calendar from, Calendar to, Interval interval) {
        super(stock.getSymbol());
        initialStockInfo(stock);

        timestamp = CalendarConverter.toString(to);
        yearsOffset = getYears(from, to);

        try {
            List<HistoricalQuote> quotes = stock.getHistory(from, to, interval);
            transformQuotes(quotes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialStockInfo(Stock stock){
        setCurrency(stock.getCurrency());
        setDividend(stock.getDividend());
        setName(stock.getName());
        setQuote(stock.getQuote());
        setStats(stock.getStats());
        setStockExchange(stock.getStockExchange());
    }

    private double getYears(Calendar from, Calendar to){
        long sub = to.getTime().getTime() - from.getTime().getTime();
        long dayUnit = 1000*3600*24;
        long days = sub / dayUnit;
        double years = days / 360.0;

        return years;
    }

    private void transformQuotes(List<HistoricalQuote> quotes){
        historicalQuotes = inverseHistoricalQuotes(quotes);
        List<BigDecimal> openList = new ArrayList<>();
        List<BigDecimal> highList = new ArrayList<>();
        List<BigDecimal> lowList = new ArrayList<>();
        List<BigDecimal> closeList = new ArrayList<>();
        List<BigDecimal> adjCloseList = new ArrayList<>();
        List<BigDecimal> volumeList = new ArrayList<>();

        for (HistoricalQuote historicalQuote : historicalQuotes) {
            if(!isInvalidQuotes(historicalQuote)) {
                openList.add(historicalQuote.getOpen());
                highList.add(historicalQuote.getHigh());
                lowList.add(historicalQuote.getLow());
                closeList.add(historicalQuote.getClose());
                adjCloseList.add(historicalQuote.getAdjClose());
                volumeList.add(new BigDecimal(historicalQuote.getVolume()));
            }
        }

        opens = new Matrix1d(openList);
        highs = new Matrix1d(highList);
        lows = new Matrix1d(lowList);
        closes = new Matrix1d(closeList);
        adjCloses = new Matrix1d(adjCloseList);
        volumes = new Matrix1d(volumeList);
    }

    private List<HistoricalQuote> inverseHistoricalQuotes(List<HistoricalQuote> quotes){
        List<HistoricalQuote> inverseQuotes = new ArrayList<>();
        for(int i = quotes.size() - 1; i >= 0; i--)
            inverseQuotes.add(quotes.get(i));
        return inverseQuotes;
    }

    private boolean isInvalidQuotes(HistoricalQuote quote){
        return quote.getClose() == null || quote.getOpen() == null ||
                quote.getHigh() == null  || quote.getLow() == null ||
                quote.getAdjClose() == null  || quote.getVolume() == null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public double getYearsOffset() {
        return yearsOffset;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Date[] getDates() {
        if(calendarDates == null)
            calendarDates = getCalendarDates();

        Date[] dates = new Date[calendarDates.length];
        for(int i = 0; i < dates.length; i++)
            dates[i] = CalendarConverter.toDate(calendarDates[i]);

        return dates;
    }

    public Calendar[] getCalendarDates() {
        if(calendarDates == null) {
            calendarDates = new Calendar[historicalQuotes.size()];
            int i = 0;
            for (HistoricalQuote historicalQuote : historicalQuotes) {
                calendarDates[i] = historicalQuote.getDate();
                i++;
            }
        }
        return calendarDates;
    }

    public Matrix1d getOpens() {
        return opens;
    }

    public Matrix1d getCloses() {
        return closes;
    }

    public Matrix1d getAdjCloses() {
        return adjCloses;
    }

    public Matrix1d getHighs() {
        return highs;
    }

    public Matrix1d getLows() {
        return lows;
    }

    public Matrix1d getVolumes() {
        return volumes;
    }

    public Matrix1d getChanges(){

        Matrix1d closes = getCloses();
        Matrix1d percentage = new Matrix1d(100, closes.size());
        Matrix1d lastDayCloses = closes.shift(1);
        Matrix1d closesSpreads = closes.subtract(lastDayCloses);

        return closesSpreads.multiply(percentage)
                .divide(lastDayCloses);
    }

    public Matrix1d getMA(int days){
        Matrix1d closes = getCloses();
        return getRollingMean(closes, days);
    }

    public Matrix1d getVolumeMA(int days){
        Matrix1d volumes = getVolumes();
        return getRollingMean(volumes, days);
    }

    public Matrix1d[] getKD(){
        int defaultDays = 9;
        return getKD(defaultDays);
    }

    public Matrix1d[] getKD(int rsvDays){

        Matrix1d closes = getCloses();
        Matrix1d percentage = new Matrix1d(100, closes.size());

        Matrix1d rollingMax = getRollingMax(closes, rsvDays);
        Matrix1d rollingMin = getRollingMin(closes, rsvDays);

        Matrix1d todayAmplitude = closes.subtract(rollingMin);
        Matrix1d maxAmplitude = rollingMax.subtract(rollingMin);

        Matrix1d RSV = todayAmplitude
                .divide(maxAmplitude)
                .multiply(percentage);

        BigDecimal[] rsvVals = RSV.toBigDecimalArray();
        BigDecimal[] k = getKdMovingAverage(rsvVals);
        BigDecimal[] d = getKdMovingAverage(k);

        return new Matrix1d[]{new Matrix1d(k), new Matrix1d(d)};
    }

    private BigDecimal[] getKdMovingAverage(BigDecimal[] inputs){
        BigDecimal oneThird = new BigDecimal("1").divide(new BigDecimal("3"), 5, BigDecimal.ROUND_HALF_UP);
        BigDecimal twoThird = new BigDecimal("2").divide(new BigDecimal("3"), 5, BigDecimal.ROUND_HALF_UP);
        BigDecimal currentValue = new BigDecimal("50");

        BigDecimal[] result = new BigDecimal[inputs.length];
        for(int i = 0; i < inputs.length; i++){
            if(inputs[i] == null){
                result[i] = null;
                continue;
            }
            currentValue = inputs[i].multiply(oneThird)
                    .add(currentValue.multiply(twoThird));

            result[i] = currentValue;
        }
        return result;
    }

    public Matrix1d[] getMACD(){
        int defaultSlowDays = 26;
        int defaultFastDays = 12;
        int defaultSmooth = 9;
        return getMACD(defaultSlowDays, defaultFastDays, defaultSmooth);
    }

    public Matrix1d[] getMACD(int slowDays, int fastDays, int smooth){
        Matrix1d closes = getCloses();

        Matrix1d EMA12 = getExponentialMovingAverage(closes.toBigDecimalArray(), fastDays);
        Matrix1d EMA26 = getExponentialMovingAverage(closes.toBigDecimalArray(), slowDays);

        Matrix1d DIF = EMA12.subtract(EMA26);
        Matrix1d DEM = getExponentialMovingAverage(DIF.toBigDecimalArray(), smooth);
        Matrix1d OSC = DIF.subtract(DEM);
        return new Matrix1d[]{DIF, DEM, OSC};
    }

    public Matrix1d[] getBollingerBands(){
        int defaultWindow = 20;

        Matrix1d closes = getCloses();

        Matrix1d twoTimesRollingStd = getRollingStd(closes, defaultWindow)
                .multiply(new Matrix1d(2, closes.size()));

        Matrix1d centerLine = getRollingMean(closes, defaultWindow);
        Matrix1d topLine = centerLine.add(twoTimesRollingStd);
        Matrix1d bottomLine = centerLine.subtract(twoTimesRollingStd);

        return new Matrix1d[]{topLine, centerLine, bottomLine};
    }

    public Matrix1d[] getChanBands(){

        BigDecimal z95 = new BigDecimal(2);
        BigDecimal z75 = new BigDecimal(1.15);

        Matrix1d closes = getCloses();
        Matrix1d centerLine = closes.simpleRegressionLine();
        BigDecimal std = closes.subtract(centerLine).std();

        Matrix1d std95 = new Matrix1d(std.multiply(z95).doubleValue(), closes.size());
        Matrix1d std75 = new Matrix1d(std.multiply(z75).doubleValue(), closes.size());

        Matrix1d lineThree = centerLine;

        Matrix1d lineTwo = lineThree.add(std75);
        Matrix1d lineOne = lineThree.add(std95);

        Matrix1d lineFour = lineThree.subtract(std75);
        Matrix1d lineFive = lineThree.subtract(std95);

        return new Matrix1d[]{lineOne, lineTwo, lineThree, lineFour, lineFive};
    }

    public Matrix1d getRSI(int days){
        Matrix1d closes = getCloses();
        Matrix1d one = new Matrix1d(1,closes.size());
        Matrix1d percentage = new Matrix1d(100, closes.size());

        Matrix1d closesSpreads = closes.subtract(closes.shift(1));
        Matrix1d spreadUps = closesSpreads.moreThan(0).multiply(closesSpreads);
        Matrix1d spreadDowns = closesSpreads.lessThan(0).multiply(closesSpreads);

        Matrix1d EMAUp = getExponentialMovingAverage(spreadUps.toBigDecimalArray(), days);
        Matrix1d EMADown = getExponentialMovingAverage(spreadDowns.toBigDecimalArray(), days);

        Matrix1d RS = EMAUp.divide(EMADown).multiply(new Matrix1d(-1, closes.size()));
        Matrix1d RSI = one.subtract(one.divide(one.add(RS))).multiply(percentage);

        return RSI;
    }

    public Matrix1d getBIAS(int days){
        Matrix1d closes = getCloses();
        Matrix1d moveingAverage = getMA(days);

        return closes.subtract(moveingAverage)
                .multiply(new Matrix1d(100, closes.size()))
                .divide(moveingAverage);
    }

    public Matrix1d getRollingMax(Matrix1d datas, int offset){
        int idx = 0;
        BigDecimal[] result = new BigDecimal[datas.size()];
        for(Matrix1d rolling : datas.rolling(offset))
            result[idx++] = rolling.max();
        return new Matrix1d(result);
    }

    public Matrix1d getRollingMin(Matrix1d datas, int offset){
        int idx = 0;
        BigDecimal[] result = new BigDecimal[datas.size()];
        for(Matrix1d rolling : datas.rolling(offset))
            result[idx++] = rolling.min();
        return new Matrix1d(result);
    }

    public Matrix1d getRollingStd(Matrix1d datas, int offset){
        int idx = 0;
        BigDecimal[] result = new BigDecimal[datas.size()];
        for(Matrix1d rolling : datas.rolling(offset))
            result[idx++] = rolling.std();
        return new Matrix1d(result);
    }

    private Matrix1d getRollingMean(Matrix1d datas, int offset){
        int idx = 0;
        BigDecimal[] result = new BigDecimal[datas.size()];
        for(Matrix1d rolling : datas.rolling(offset))
            result[idx++] = rolling.mean();
        return new Matrix1d(result);
    }

    private Matrix1d getExponentialMovingAverage(BigDecimal[] datas, int days){
        BigDecimal alpha = new BigDecimal("2").divide(new BigDecimal(1 + days), 5, BigDecimal.ROUND_HALF_UP);
        BigDecimal oneSubtractAlpha = BigDecimal.ONE.subtract(alpha);
        BigDecimal average = BigDecimal.ZERO;

        BigDecimal[] result = new BigDecimal[datas.length];
        for(int i = 0; i < datas.length; i++){
            if(datas[i] != null)
                average = alpha.multiply(datas[i])
                        .add(oneSubtractAlpha.multiply(average));

            if(i < days - 1 || datas[i] == null) {
                result[i] = null;
                continue;
            }


            result[i] = average;
        }
        return new Matrix1d(result);
    }

}

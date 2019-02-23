package studio.bluekitten.backtestingcat.core.strategies;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Spinner;

import studio.bluekitten.backtestingcat.R;
import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;

public class KbarShadow implements Strategy{

    private String shadow, bar;

    public KbarShadow(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        Spinner shadowSpinner = (Spinner)dialog.findViewById(R.id.KBarShadowShadow);
        shadow = shadowSpinner.getSelectedItem().toString();

        Spinner barSpinner = (Spinner)dialog.findViewById(R.id.KBarShadowKbar);
        bar = barSpinner.getSelectedItem().toString();
    }

    public KbarShadow(String shadow, String bar){
        this.shadow = shadow;
        this.bar = bar;
    }

    @Override
    public String descriptions() {
        return "當日為" + shadow + bar;
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        return getBarSignal(stock).and(getShadowSignal(stock));
    }

    private Matrix1d getShadowSignal(StockB stock){
        Matrix1d closes = stock.getCloses();
        Matrix1d opens = stock.getOpens();

        Matrix1d higherPrices = getHigherPrices(opens, closes);
        Matrix1d LowerPrices = getLowerPrices(opens, closes);

        Matrix1d highs = stock.getHighs();
        Matrix1d lows = stock.getLows();

        if("上影線".equals(shadow)){
            Matrix1d signals1 = higherPrices.lessThan(highs);
            Matrix1d signals2 = LowerPrices.equalTo(lows);
            return signals1.and(signals2);
        }
        else if("下影線".equals(shadow)){
            Matrix1d signals1 = higherPrices.equalTo(highs);
            Matrix1d signals2 = LowerPrices.moreThan(lows);
            return signals1.and(signals2);
        }
        else if("上下影線".equals(shadow)){
            Matrix1d signals1 = higherPrices.lessThan(highs);
            Matrix1d signals2 = LowerPrices.moreThan(lows);
            return signals1.and(signals2);
        }
        else if("無影線".equals(shadow)){
            Matrix1d signals1 = higherPrices.equalTo(highs);
            Matrix1d signals2 = LowerPrices.equalTo(lows);
            return signals1.and(signals2);
        }
        else if("十字線".equals(shadow)){
            Matrix1d signals1 = higherPrices.lessThan(highs);
            Matrix1d signals2 = LowerPrices.moreThan(lows);
            Matrix1d signals3 = higherPrices.equalTo(LowerPrices);
            return signals1.and(signals2).and(signals3);
        }
        else if("T字線".equals(shadow)){
            Matrix1d signals1 = higherPrices.equalTo(highs);
            Matrix1d signals2 = LowerPrices.moreThan(lows);
            Matrix1d signals3 = higherPrices.equalTo(LowerPrices);
            return signals1.and(signals2).and(signals3);
        }
        else if("倒T字線".equals(shadow)){
            Matrix1d signals1 = higherPrices.lessThan(highs);
            Matrix1d signals2 = LowerPrices.equalTo(lows);
            Matrix1d signals3 = higherPrices.equalTo(LowerPrices);
            return signals1.and(signals2).and(signals3);
        }
        // 一字線
        else{
            Matrix1d signals1 = higherPrices.equalTo(highs);
            Matrix1d signals2 = LowerPrices.equalTo(lows);
            Matrix1d signals3 = higherPrices.equalTo(LowerPrices);
            return signals1.and(signals2).and(signals3);
        }
    }

    private Matrix1d getHigherPrices(Matrix1d opens, Matrix1d closes){
        Matrix1d barSignals = closes.moreThan(opens);
        Matrix1d colseHighers = barSignals.multiply(closes);
        Matrix1d openHighers = barSignals.not().multiply(opens);
        return colseHighers.add(openHighers);
    }

    private Matrix1d getLowerPrices(Matrix1d opens, Matrix1d closes){
        Matrix1d barSignals = closes.lessThan(opens);
        Matrix1d colseLowers = barSignals.multiply(closes);
        Matrix1d openLowers = barSignals.not().multiply(opens);
        return colseLowers.add(openLowers);
    }

    private Matrix1d getBarSignal(StockB stock){
        Matrix1d closes = stock.getCloses();
        Matrix1d opens = stock.getOpens();
        Matrix1d closesSpreads = closes.subtract(opens);

        if("紅K".equals(bar))
            return closesSpreads.moreThan(0);

        else
            return closesSpreads.lessThan(0);
    }

    public static final Parcelable.Creator<KbarShadow> CREATOR=new Creator<KbarShadow>() {
        @Override
        public KbarShadow createFromParcel(Parcel parcel) {
            String shadow = parcel.readString();
            String bar = parcel.readString();
            return new KbarShadow(shadow, bar);
        }

        @Override
        public KbarShadow[] newArray(int i) {
            return new KbarShadow[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shadow);
        parcel.writeString(bar);
    }
}

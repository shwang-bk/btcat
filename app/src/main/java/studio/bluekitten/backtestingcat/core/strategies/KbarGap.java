package studio.bluekitten.backtestingcat.core.strategies;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

import studio.bluekitten.backtestingcat.R;
import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;

public class KbarGap implements Strategy {
    private int days;

    public KbarGap(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        EditText dayText = (EditText)dialog.findViewById(R.id.KBarGapDays);
        days = Integer.parseInt(dayText.getText().toString());
    }

    public KbarGap(int days){
        this.days = days;
    }

    @Override
    public String descriptions() {
        return "連續" + days + "天跳空";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d opens = stock.getOpens();
        Matrix1d closes = stock.getCloses();
        Matrix1d higherPrices = getHigherPrices(opens, closes);
        Matrix1d lowerPrices = getLowerPrices(opens, closes);

        Matrix1d lastHigherPrices = getHigherPrices(opens.shift(1), closes.shift(1));
        Matrix1d lastlowerPrices = getLowerPrices(opens.shift(1), closes.shift(1));

        Matrix1d signals1 = lowerPrices.moreThan(lastlowerPrices)
                .and(lowerPrices.moreThan(lastHigherPrices));

        Matrix1d signals2 = lastlowerPrices.moreThan(lowerPrices)
                .and(lastlowerPrices.moreThan(higherPrices));

        int[] continueDatas = signals1.or(signals2).toIntegerArray();

        int[] results = new int[continueDatas.length];
        int continueDays = 0;
        for(int i = 0; i < continueDatas.length; i++){
            if(continueDatas[i] == 0)
                continueDays = 0;
            else
                continueDays++;

            results[i] = continueDays >= days ? 1 : 0;
        }
        return new Matrix1d(results);
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

    public static final Parcelable.Creator<KbarGap> CREATOR = new Creator<KbarGap>(){

        @Override
        public KbarGap createFromParcel(Parcel parcel) {
            int days = parcel.readInt();
            return new KbarGap(days);
        }

        @Override
        public KbarGap[] newArray(int i) {
            return new KbarGap[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(days);
    }
}

package studio.bluekitten.backtestingcat.core.strategies;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Parcel;
import android.widget.EditText;

import studio.bluekitten.backtestingcat.R;
import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;

public class BbandTouchBottom implements Strategy {
    private int days;

    public BbandTouchBottom(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        EditText dayText = (EditText)dialog.findViewById(R.id.BbandTouchBottomDays);
        days = Integer.parseInt(dayText.getText().toString());
    }

    public BbandTouchBottom(int days){
        this.days = days;
    }

    @Override
    public String descriptions() {
        return "最低價連續" + days + "日突破布林通到底";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d lows = stock.getLows();
        Matrix1d[] bband = stock.getBollingerBands();

        Matrix1d singleDaySignals = lows.lessThan(bband[StockB.BBAND_BOTTOM_LINE]);
        Matrix1d results = new Matrix1d(1, singleDaySignals.size());
        for(int i = 0; i < days; i++){
            results = results.and(singleDaySignals);
            singleDaySignals = singleDaySignals.shift(1);
        }

        return results;
    }

    public static final Creator<BbandTouchBottom> CREATOR = new Creator<BbandTouchBottom>(){

        @Override
        public BbandTouchBottom createFromParcel(Parcel parcel) {
            int days = parcel.readInt();
            return new BbandTouchBottom(days);
        }

        @Override
        public BbandTouchBottom[] newArray(int i) {
            return new BbandTouchBottom[i];
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

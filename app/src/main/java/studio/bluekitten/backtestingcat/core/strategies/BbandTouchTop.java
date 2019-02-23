package studio.bluekitten.backtestingcat.core.strategies;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Parcel;
import android.widget.EditText;

import studio.bluekitten.backtestingcat.R;
import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;

public class BbandTouchTop implements Strategy {
    private int days;

    public BbandTouchTop(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        EditText dayText = (EditText)dialog.findViewById(R.id.BbandTouchTopDays);
        days = Integer.parseInt(dayText.getText().toString());
    }

    public BbandTouchTop(int days){
        this.days = days;
    }

    @Override
    public String descriptions() {
        return "最高價連續" + days + "日突破布林通到頂";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d highs = stock.getHighs();
        Matrix1d[] bband = stock.getBollingerBands();

        Matrix1d singleDaySignals = highs.moreThan(bband[StockB.BBAND_TOP_LINE]);
        Matrix1d results = new Matrix1d(1, singleDaySignals.size());
        for(int i = 0; i < days; i++){
            results = results.and(singleDaySignals);
            singleDaySignals = singleDaySignals.shift(1);
        }

        return results;
    }

    public static final Creator<BbandTouchTop> CREATOR = new Creator<BbandTouchTop>(){

        @Override
        public BbandTouchTop createFromParcel(Parcel parcel) {
            int days = parcel.readInt();
            return new BbandTouchTop(days);
        }

        @Override
        public BbandTouchTop[] newArray(int i) {
            return new BbandTouchTop[i];
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

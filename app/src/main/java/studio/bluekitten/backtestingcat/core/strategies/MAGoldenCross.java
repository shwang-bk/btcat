package studio.bluekitten.backtestingcat.core.strategies;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

import studio.bluekitten.backtestingcat.R;
import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;

public class MAGoldenCross implements Strategy {
    private int slowDays, fastDays;

    public MAGoldenCross(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        EditText slowText = (EditText)dialog.findViewById(R.id.MAGoldenSlowVal);
        slowDays = Integer.parseInt(slowText.getText().toString());

        EditText fastText = (EditText)dialog.findViewById(R.id.MAGoldenFastVal);
        fastDays = Integer.parseInt(fastText.getText().toString());
    }

    public MAGoldenCross(int slowDays, int fastDays){
        this.slowDays = slowDays;
        this.fastDays = fastDays;
    }

    @Override
    public String descriptions() {
        return "均線黃金交叉(" + fastDays + "日, " + slowDays + "日)";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d MAFast = stock.getMA(fastDays);
        Matrix1d MASlow = stock.getMA(slowDays);
        return MAFast.moreThan(MASlow);
    }

    public static final Parcelable.Creator<MAGoldenCross> CREATOR = new Creator<MAGoldenCross>() {
        @Override
        public MAGoldenCross createFromParcel(Parcel parcel) {
            int slow = parcel.readInt();
            int fast = parcel.readInt();
            return new MAGoldenCross(slow, fast);
        }

        @Override
        public MAGoldenCross[] newArray(int i) {
            return new MAGoldenCross[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(slowDays);
        parcel.writeInt(fastDays);
    }
}

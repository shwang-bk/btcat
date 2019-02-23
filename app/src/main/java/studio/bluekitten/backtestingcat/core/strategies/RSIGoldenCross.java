package studio.bluekitten.backtestingcat.core.strategies;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

import studio.bluekitten.backtestingcat.R;
import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;

public class RSIGoldenCross implements Strategy {
    private int slowDays, fastDays;

    public RSIGoldenCross(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        EditText slowText = (EditText)dialog.findViewById(R.id.RSIGoldenSlowVal);
        slowDays = Integer.parseInt(slowText.getText().toString());

        EditText fastText = (EditText)dialog.findViewById(R.id.RSIGoldenFastVal);
        fastDays = Integer.parseInt(fastText.getText().toString());
    }

    public RSIGoldenCross(int slowDays, int fastDays){
        this.slowDays = slowDays;
        this.fastDays = fastDays;
    }

    @Override
    public String descriptions() {
        return "RSI黃金交叉(" + fastDays + "日, " + slowDays + "日)";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d RSIFast = stock.getMA(fastDays);
        Matrix1d RSISlow = stock.getMA(slowDays);
        return RSIFast.moreThan(RSISlow);
    }

    public static final Parcelable.Creator<RSIGoldenCross> CREATOR = new Creator<RSIGoldenCross>() {
        @Override
        public RSIGoldenCross createFromParcel(Parcel parcel) {
            int slow = parcel.readInt();
            int fast = parcel.readInt();
            return new RSIGoldenCross(slow, fast);
        }

        @Override
        public RSIGoldenCross[] newArray(int i) {
            return new RSIGoldenCross[i];
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

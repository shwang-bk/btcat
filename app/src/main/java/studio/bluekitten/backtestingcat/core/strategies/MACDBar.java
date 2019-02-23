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

public class MACDBar implements Strategy {
    public String macdbar;

    public MACDBar(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        Spinner macdbarSpinner = (Spinner)dialog.findViewById(R.id.macdBar);
        macdbar = macdbarSpinner.getSelectedItem().toString();
    }

    public MACDBar(String macdbar){
        this.macdbar = macdbar;
    }

    @Override
    public String descriptions() {
        return "MACD柱狀圖呈" + macdbar + "反轉";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d[] MACD = stock.getMACD();
        Matrix1d OSC = MACD[StockB.OSC];
        Matrix1d LastDayOSC = OSC.shift(1);

        if("向下".equals(macdbar))
            return OSC.lessThan(LastDayOSC);
        else
            return OSC.moreThan(LastDayOSC);
    }

    public static final Parcelable.Creator<MACDBar> CREATOR = new Creator<MACDBar>() {
        @Override
        public MACDBar createFromParcel(Parcel parcel) {
            String macdbar = parcel.readString();
            return new MACDBar(macdbar);
        }

        @Override
        public MACDBar[] newArray(int i) {
            return new MACDBar[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(macdbar);
    }
}

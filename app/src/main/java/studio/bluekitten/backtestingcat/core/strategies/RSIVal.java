package studio.bluekitten.backtestingcat.core.strategies;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;
import android.widget.Spinner;

import studio.bluekitten.backtestingcat.R;
import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;

public class RSIVal implements Strategy{
    String measurement;
    int rsi, days;

    public RSIVal(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        EditText daysText = (EditText)dialog.findViewById(R.id.RSIDays);
        days = Integer.parseInt(daysText.getText().toString());

        EditText rsiText = (EditText)dialog.findViewById(R.id.RSIVal);
        rsi = Integer.parseInt(rsiText.getText().toString());

        Spinner measurementSpinner = (Spinner)dialog.findViewById(R.id.RSIMeasurement);
        measurement = measurementSpinner.getSelectedItem().toString();
    }

    public RSIVal(int days, String measurement, int rsi){
        this.days = days;
        this.measurement = measurement;
        this.rsi = rsi;
    }

    @Override
    public String descriptions() {
        return days + " 日RSI " + measurement + " " + rsi;
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d RSI = stock.getRSI(days);
        if("<".equals(measurement))
            return RSI.lessThan(rsi);
        else
            return RSI.moreThan(rsi);
    }

    public static final Parcelable.Creator<RSIVal> CREATOR = new Creator<RSIVal>() {
        @Override
        public RSIVal createFromParcel(Parcel parcel) {
            int days = parcel.readInt();
            String measurement = parcel.readString();
            int rsi = parcel.readInt();

            return new RSIVal(days, measurement, rsi);
        }

        @Override
        public RSIVal[] newArray(int i) {
            return new RSIVal[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(days);
        parcel.writeString(measurement);
        parcel.writeInt(rsi);
    }
}

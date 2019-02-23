package studio.bluekitten.backtestingcat.core.strategies;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Parcel;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import studio.bluekitten.backtestingcat.R;
import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;
import studio.bluekitten.backtestingcat.util.TestActivity;

public class BIASVal implements Strategy{
    String measurement;
    int bias, days;

    public BIASVal(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        EditText daysText = (EditText)dialog.findViewById(R.id.BIASDays);
        days = Integer.parseInt(daysText.getText().toString());

        EditText rsiText = (EditText)dialog.findViewById(R.id.BIASVal);
        bias = Integer.parseInt(rsiText.getText().toString());

        Spinner measurementSpinner = (Spinner)dialog.findViewById(R.id.BIASMeasurement);
        measurement = measurementSpinner.getSelectedItem().toString();
    }

    public BIASVal(int days, String measurement, int bias){
        this.days = days;
        this.measurement = measurement;
        this.bias = bias;
    }

    @Override
    public String descriptions() {
        return days + " 日乖離率 " + measurement + " " + bias;
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d BIAS = stock.getBIAS(days);

        Log.d(TestActivity.TEST_LOG, "" + BIAS);
        if("<".equals(measurement))
            return BIAS.lessThan(bias);
        else
            return BIAS.moreThan(bias);
    }

    public static final Creator<BIASVal> CREATOR = new Creator<BIASVal>() {
        @Override
        public BIASVal createFromParcel(Parcel parcel) {
            int days = parcel.readInt();
            String measurement = parcel.readString();
            int rsi = parcel.readInt();

            return new BIASVal(days, measurement, rsi);
        }

        @Override
        public BIASVal[] newArray(int i) {
            return new BIASVal[i];
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
        parcel.writeInt(bias);
    }
}

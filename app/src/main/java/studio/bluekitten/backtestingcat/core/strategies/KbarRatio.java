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

public class KbarRatio implements Strategy{

    private String measurement, change;
    private int value;

    public KbarRatio(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        Spinner changeSpinner = (Spinner) dialog.findViewById(R.id.KBarRatioChanges);
        change = changeSpinner.getSelectedItem().toString();

        Spinner measurementSpinner = (Spinner) dialog.findViewById(R.id.KBarRatioMeasurement);
        measurement = measurementSpinner.getSelectedItem().toString();

        EditText valueText = (EditText) dialog.findViewById(R.id.KBarRatioValue);
        value = Integer.parseInt(valueText.getText().toString());
    }

    public KbarRatio(String change, String measurement, int value){
        this.change = change;
        this.measurement = measurement;
        this.value = value;
    }

    @Override
    public String descriptions() {
        return "當日" + change + " " + measurement + " " + value + " %";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {

        Matrix1d spreadRatios = stock.getChanges();

        if("跌幅".equals(change))
            spreadRatios = spreadRatios.multiply(new Matrix1d(-1, spreadRatios.size()));

        if("<".equals(measurement))
            return spreadRatios.lessThan(value);
        else
            return spreadRatios.moreThan(value);
    }

    public static final Parcelable.Creator<KbarRatio> CREATOR = new Creator<KbarRatio>() {
        @Override
        public KbarRatio createFromParcel(Parcel parcel) {
            String change = parcel.readString();
            String measurement = parcel.readString();
            int value = parcel.readInt();
            return new KbarRatio(change, measurement, value);
        }

        @Override
        public KbarRatio[] newArray(int i) {
            return new KbarRatio[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(change);
        parcel.writeString(measurement);
        parcel.writeInt(value);
    }
}

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

public class VolumeMA implements Strategy {

    private int days, value;
    private String measurement;

    public VolumeMA(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        EditText dayText = (EditText)dialog.findViewById(R.id.volumeMADays);
        days = Integer.parseInt(dayText.getText().toString());

        Spinner measurementSpinner = (Spinner)dialog.findViewById(R.id.volumeMAMeasurement);
        measurement = measurementSpinner.getSelectedItem().toString();

        EditText valueText = (EditText)dialog.findViewById(R.id.volumeMAVal);
        value = Integer.parseInt(valueText.getText().toString());
    }

    public VolumeMA(int days, String measurement, int value){
        this.days = days;
        this.measurement = measurement;
        this.value = value;
    }

    @Override
    public String descriptions() {
        return "" + days + " 日成交均量 " + measurement + " " + value + " 張";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d volumeMA = stock.getVolumeMA(days);
        if("<".equals(measurement))
            return volumeMA.lessThan(value);
        else
            return volumeMA.moreThan(value);
    }

    public static final Parcelable.Creator<VolumeMA> CREATOR = new Creator<VolumeMA>() {
        @Override
        public VolumeMA createFromParcel(Parcel parcel) {
            int days = parcel.readInt();
            String measurement = parcel.readString();
            int value = parcel.readInt();
            return new VolumeMA(days, measurement, value);
        }

        @Override
        public VolumeMA[] newArray(int i) {
            return new VolumeMA[i];
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
        parcel.writeInt(value);
    }
}

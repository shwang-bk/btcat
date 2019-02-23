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

public class VolumeContinue implements Strategy {
    private int days, value;
    private String measurement;

    public VolumeContinue(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        EditText dayText = (EditText)dialog.findViewById(R.id.volumeContinueDays);
        days = Integer.parseInt(dayText.getText().toString());

        Spinner measurementSpinner = (Spinner)dialog.findViewById(R.id.volumeContinueMeasurement);
        measurement = measurementSpinner.getSelectedItem().toString();

        EditText valueText = (EditText)dialog.findViewById(R.id.volumeContinueVal);
        value = Integer.parseInt(valueText.getText().toString());

    }

    public VolumeContinue(int days, String measurement, int value){
        this.days = days;
        this.measurement = measurement;
        this.value = value;
    }

    @Override
    public String descriptions() {
        return "成交量連續 " + days + " 日 " + measurement + " " + value + " 張";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d volumes = stock.getVolumes();
        int[] continueDatas;

        if("<".equals(measurement))
            continueDatas = volumes.lessThan(value).toIntegerArray();
        else
            continueDatas = volumes.moreThan(value).toIntegerArray();

        int[] results = new int[volumes.size()];
        int continueDays = 0;
        for(int i = 0; i < volumes.size(); i++){
            if(continueDatas[i] == 0)
                continueDays = 0;
            else
                continueDays++;

            results[i] = continueDays >= days ? 1 : 0;
        }
        return new Matrix1d(results);
    }

    public static final Parcelable.Creator<VolumeContinue> CREATOR = new Creator<VolumeContinue>() {
        @Override
        public VolumeContinue createFromParcel(Parcel parcel) {
            int days = parcel.readInt();
            String measurement = parcel.readString();
            int value = parcel.readInt();
            return new VolumeContinue(days, measurement, value);
        }

        @Override
        public VolumeContinue[] newArray(int i) {
            return new VolumeContinue[i];
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

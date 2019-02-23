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

public class KDD implements Strategy {
    private String measurement;
    private int d;

    public KDD(DialogInterface dialogInterface){

        Dialog dialog = (Dialog) dialogInterface;

        EditText dText = (EditText)dialog.findViewById(R.id.KDDVal);
        d = Integer.parseInt(dText.getText().toString());

        Spinner measurementSpinner = (Spinner)dialog.findViewById(R.id.KDDMeasurement);
        measurement = measurementSpinner.getSelectedItem().toString();
    }

    public KDD(String measurement, int d){
        this.measurement = measurement;
        this.d = d;
    }

    @Override
    public String descriptions() {
        return "D " + measurement + " " + d;
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d[] KD = stock.getKD();
        if("<".equals(measurement))
            return KD[StockB.D].lessThan(d);
        else
            return KD[StockB.D].moreThan(d);
    }

    public static final Parcelable.Creator<KDD> CREATOR = new Creator<KDD>() {
        @Override
        public KDD createFromParcel(Parcel parcel) {
            String measurement = parcel.readString();
            int d = parcel.readInt();
            return new KDD(measurement, d);
        }

        @Override
        public KDD[] newArray(int i) {
            return new KDD[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(measurement);
        parcel.writeInt(d);
    }
}

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

public class KDK implements Strategy {
    private String measurement;
    private int k;

    public KDK(DialogInterface dialogInterface){

        Dialog dialog = (Dialog) dialogInterface;

        EditText kText = (EditText)dialog.findViewById(R.id.KDKVal);
        k = Integer.parseInt(kText.getText().toString());

        Spinner measurementSpinner = (Spinner)dialog.findViewById(R.id.KDKMeasurement);
        measurement = measurementSpinner.getSelectedItem().toString();

    }

    public KDK(String measurement, int k){
        this.measurement = measurement;
        this.k = k;
    }

    @Override
    public String descriptions() {
        return "K " + measurement + " " + k;
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d[] KD = stock.getKD();
        if("<".equals(measurement))
            return KD[StockB.K].lessThan(k);
        else
            return KD[StockB.K].moreThan(k);
    }

    public static final Parcelable.Creator<KDK> CREATOR = new Creator<KDK>() {
        @Override
        public KDK createFromParcel(Parcel parcel) {
            String measurement = parcel.readString();
            int k = parcel.readInt();
            return new KDK(measurement, k);
        }

        @Override
        public KDK[] newArray(int i) {
            return new KDK[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(measurement);
        parcel.writeInt(k);
    }
}

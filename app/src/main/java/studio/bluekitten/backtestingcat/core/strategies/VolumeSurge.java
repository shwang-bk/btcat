package studio.bluekitten.backtestingcat.core.strategies;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

import studio.bluekitten.backtestingcat.R;
import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;

/**
 * Created by m1021 on 2017/10/11.
 */

public class VolumeSurge implements Strategy {
    private int days, value;

    public VolumeSurge(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        EditText dayText = (EditText)dialog.findViewById(R.id.volumeSurgeDays);
        days = Integer.parseInt(dayText.getText().toString());

        EditText valueText = (EditText)dialog.findViewById(R.id.volumeSurgeVal);
        value = Integer.parseInt(valueText.getText().toString());
    }

    public VolumeSurge(int days, int value){
        this.days = days;
        this.value = value;
    }

    @Override
    public String descriptions() {
        return "當日成交量 > " + days + " 日成交均量 " + value + " %";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        double times = (double)value / 100;
        Matrix1d volume = stock.getVolumes();
        Matrix1d surgeThreshold = stock.getVolumeMA(days).multiply(new Matrix1d(times, volume.size()));
        return surgeThreshold.lessThan(volume);
    }

    public static final Parcelable.Creator<VolumeSurge> CREATOR = new Creator<VolumeSurge>() {
        @Override
        public VolumeSurge createFromParcel(Parcel parcel) {
            int days = parcel.readInt();
            int value = parcel.readInt();
            return new VolumeSurge(days, value);
        }

        @Override
        public VolumeSurge[] newArray(int i) {
            return new VolumeSurge[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(days);
        dest.writeInt(value);
    }
}

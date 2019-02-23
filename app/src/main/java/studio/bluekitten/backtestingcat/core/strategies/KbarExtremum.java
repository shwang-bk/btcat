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

public class KbarExtremum implements Strategy {

    private int days;
    private String direction;

    public KbarExtremum(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        EditText dayText = (EditText)dialog.findViewById(R.id.KBarExtremumDays);
        days = Integer.parseInt(dayText.getText().toString());

        Spinner directionSpinner = (Spinner)dialog.findViewById(R.id.KBarExtremumDirection);
        direction = directionSpinner.getSelectedItem().toString();
    }

    public KbarExtremum(int days, String direction){
        this.days = days;
        this.direction = direction;
    }

    @Override
    public String descriptions() {
        return "突破 " + days + " 日" + direction;
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d colses = stock.getCloses();
        if("新高".equals(direction)) {
            Matrix1d rollingMax = stock.getRollingMax(colses, days);
            return rollingMax.equalTo(colses);
        }
        else{
            Matrix1d rollingMin = stock.getRollingMin(colses, days);
            return rollingMin.equalTo(colses);
        }
    }

    public static final Parcelable.Creator<KbarExtremum> CREATOR = new Creator<KbarExtremum>(){

        @Override
        public KbarExtremum createFromParcel(Parcel parcel) {
            int days = parcel.readInt();
            String direction = parcel.readString();
            return new KbarExtremum(days, direction);
        }

        @Override
        public KbarExtremum[] newArray(int i) {
            return new KbarExtremum[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(days);
        parcel.writeString(direction);
    }
}

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

public class KbarContinue implements Strategy {

    private int days;
    private String bar;

    public KbarContinue(DialogInterface dialogInterface){
        Dialog dialog = (Dialog) dialogInterface;

        EditText dayText = (EditText)dialog.findViewById(R.id.KBarContinueDays);
        days = Integer.parseInt(dayText.getText().toString());

        Spinner barSpinner = (Spinner)dialog.findViewById(R.id.KBarContinueBars);
        bar = barSpinner.getSelectedItem().toString();
    }

    public KbarContinue(int days, String bar){
        this.days = days;
        this.bar = bar;
    }

    @Override
    public String descriptions() {
        return "連續" + days + "日" + bar;
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d opens = stock.getOpens();
        Matrix1d closes = stock.getCloses();
        Matrix1d daySpreads = closes.subtract(opens);
        int[] continueDatas;

        if("紅K".equals(bar))
            continueDatas = daySpreads.moreThan(0).toIntegerArray();
        else
            continueDatas = daySpreads.lessThan(0).toIntegerArray();

        int[] results = new int[daySpreads.size()];
        int continueDays = 0;
        for(int i = 0; i < daySpreads.size(); i++){
            if(continueDatas[i] == 0)
                continueDays = 0;
            else
                continueDays++;

            results[i] = continueDays >= days ? 1 : 0;
        }
        return new Matrix1d(results);
    }

    public static final Parcelable.Creator<KbarContinue> CREATOR = new Creator<KbarContinue>(){

        @Override
        public KbarContinue createFromParcel(Parcel parcel) {
            int days = parcel.readInt();
            String bar = parcel.readString();

            return new KbarContinue(days, bar);
        }

        @Override
        public KbarContinue[] newArray(int i) {
            return new KbarContinue[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(days);
        parcel.writeString(bar);
    }
}

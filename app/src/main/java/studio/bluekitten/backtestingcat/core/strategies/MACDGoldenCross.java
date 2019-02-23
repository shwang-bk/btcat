package studio.bluekitten.backtestingcat.core.strategies;

import android.os.Parcel;
import android.os.Parcelable;

import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;

public class MACDGoldenCross implements Strategy {
    @Override
    public String descriptions() {
        return "MACD黃金交叉";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d[] MACD = stock.getMACD();
        return MACD[StockB.OSC].moreThan(0);
    }

    public static final Parcelable.Creator<MACDGoldenCross> CREATOR = new Creator<MACDGoldenCross>() {
        @Override
        public MACDGoldenCross createFromParcel(Parcel parcel) {
            return new MACDGoldenCross();
        }

        @Override
        public MACDGoldenCross[] newArray(int i) {
            return new MACDGoldenCross[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}

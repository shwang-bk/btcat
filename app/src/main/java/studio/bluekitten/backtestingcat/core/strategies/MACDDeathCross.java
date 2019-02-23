package studio.bluekitten.backtestingcat.core.strategies;

import android.os.Parcel;
import android.os.Parcelable;

import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;

public class MACDDeathCross implements Strategy{
    @Override
    public String descriptions() {
        return "MACD死亡交叉";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d[] MACD = stock.getMACD();
        return MACD[StockB.OSC].lessThan(0);
    }

    public static final Parcelable.Creator<MACDDeathCross> CREATOR = new Creator<MACDDeathCross>() {
        @Override
        public MACDDeathCross createFromParcel(Parcel parcel) {
            return new MACDDeathCross();
        }

        @Override
        public MACDDeathCross[] newArray(int i) {
            return new MACDDeathCross[i];
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

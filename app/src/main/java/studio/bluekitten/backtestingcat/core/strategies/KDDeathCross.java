package studio.bluekitten.backtestingcat.core.strategies;

import android.os.Parcel;
import android.os.Parcelable;

import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;

public class KDDeathCross implements Strategy {
    @Override
    public String descriptions() {
        return "KD死亡交叉";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d[] KD = stock.getKD();
        return KD[StockB.K].lessThan(KD[StockB.D]);
    }

    public static final Parcelable.Creator<KDDeathCross> CREATOR = new Creator<KDDeathCross>() {
        @Override
        public KDDeathCross createFromParcel(Parcel parcel) {
            return new KDDeathCross();
        }

        @Override
        public KDDeathCross[] newArray(int i) {
            return new KDDeathCross[i];
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

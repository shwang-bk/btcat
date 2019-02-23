package studio.bluekitten.backtestingcat.core.strategies;

import android.os.Parcel;
import android.os.Parcelable;

import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;

public class KDGoldenCross implements Strategy {
    @Override
    public String descriptions() {
        return "KD黃金交叉";
    }

    @Override
    public Matrix1d getSignals(StockB stock) {
        Matrix1d[] KD = stock.getKD();
        return KD[StockB.K].moreThan(KD[StockB.D]);
    }

    public static final Parcelable.Creator<KDGoldenCross> CREATOR = new Creator<KDGoldenCross>() {
        @Override
        public KDGoldenCross createFromParcel(Parcel parcel) {
            return new KDGoldenCross();
        }

        @Override
        public KDGoldenCross[] newArray(int i) {
            return new KDGoldenCross[i];
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

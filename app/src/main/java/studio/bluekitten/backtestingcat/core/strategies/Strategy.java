package studio.bluekitten.backtestingcat.core.strategies;

import android.os.Parcelable;

import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.util.Matrix1d;

public interface Strategy extends Parcelable {
    public static final int BUY_STOCK = 1;
    public static final int SELL_STOCK = -1;
    public String descriptions();
    public Matrix1d getSignals(StockB stock);
}

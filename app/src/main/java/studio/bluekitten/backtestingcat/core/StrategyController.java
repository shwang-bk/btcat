package studio.bluekitten.backtestingcat.core;

import android.content.Context;
import android.content.DialogInterface;

import studio.bluekitten.backtestingcat.R;
import studio.bluekitten.backtestingcat.core.strategies.BIASVal;
import studio.bluekitten.backtestingcat.core.strategies.BbandTouchBottom;
import studio.bluekitten.backtestingcat.core.strategies.BbandTouchTop;
import studio.bluekitten.backtestingcat.core.strategies.KDD;
import studio.bluekitten.backtestingcat.core.strategies.KDDeathCross;
import studio.bluekitten.backtestingcat.core.strategies.KDGoldenCross;
import studio.bluekitten.backtestingcat.core.strategies.KDK;
import studio.bluekitten.backtestingcat.core.strategies.KbarContinue;
import studio.bluekitten.backtestingcat.core.strategies.KbarExtremum;
import studio.bluekitten.backtestingcat.core.strategies.KbarGap;
import studio.bluekitten.backtestingcat.core.strategies.KbarRatio;
import studio.bluekitten.backtestingcat.core.strategies.KbarShadow;
import studio.bluekitten.backtestingcat.core.strategies.MACDBar;
import studio.bluekitten.backtestingcat.core.strategies.MACDDeathCross;
import studio.bluekitten.backtestingcat.core.strategies.MACDGoldenCross;
import studio.bluekitten.backtestingcat.core.strategies.MADeathCross;
import studio.bluekitten.backtestingcat.core.strategies.MAGoldenCross;
import studio.bluekitten.backtestingcat.core.strategies.RSIDeathCross;
import studio.bluekitten.backtestingcat.core.strategies.RSIGoldenCross;
import studio.bluekitten.backtestingcat.core.strategies.RSIVal;
import studio.bluekitten.backtestingcat.core.strategies.Strategy;
import studio.bluekitten.backtestingcat.core.strategies.VolumeContinue;
import studio.bluekitten.backtestingcat.core.strategies.VolumeMA;
import studio.bluekitten.backtestingcat.core.strategies.VolumeSurge;

// Singleton
public class StrategyController {

    public static final String FLAG_STRATEGY = "strategy";
    public static final String FLAG_IN_STRATEGYS = "strategysin";
    public static final String FLAG_OUT_STRATEGYS = "strategysout";

    private static StrategyController strategyController = new StrategyController();
    public static StrategyController getInstence() {
        return strategyController;
    }

    private String[] strategyNames;

    private final int[] STRATEGY_DIALOG_LAYOUT = new int[]{
            R.layout.dialog_kbar_continue, R.layout.dialog_kbar_shadow, R.layout.dialog_kbar_ratio, R.layout.dialog_kbar_gap, R.layout.dialog_kbar_extremum,
            R.layout.dialog_volume_ma, R.layout.dialog_volume_continue, R.layout.dialog_volume_surge,
            R.layout.dialog_ma_golden_cross, R.layout.dialog_ma_death_cross,
            R.layout.dialog_kd_k, R.layout.dialog_kd_d, R.layout.dialog_kd_golden_cross, R.layout.dialog_kd_death_cross,
            R.layout.dialog_macd_bar, R.layout.dialog_macd_golden_cross, R.layout.dialog_macd_death_cross,
            R.layout.dialog_rsi_val, R.layout.dialog_rsi_golden_cross, R.layout.dialog_rsi_death_cross,
            R.layout.dialog_bias_val,
            R.layout.dialog_bband_top, R.layout.dialog_bband_bottom
    };


    public int getLayoutId(int i){
        return STRATEGY_DIALOG_LAYOUT[i];
    }

    //從 Strings.xml 取得 Strategy detial 名稱
    public String[] getStrategyNames(Context context){
        if(strategyNames == null){
            strategyNames = context.getResources().getStringArray(R.array.strategies);
        }
        context = null;
        return strategyNames;
    }

    // 從 Dialog 建立策略後回傳
    public Strategy createStrategy(int i, DialogInterface dialog){

        int id = STRATEGY_DIALOG_LAYOUT[i];
        switch(id){
            case R.layout.dialog_kbar_continue:     return new KbarContinue(dialog);
            case R.layout.dialog_kbar_gap:          return new KbarGap(dialog);
            case R.layout.dialog_kbar_ratio:        return new KbarRatio(dialog);
            case R.layout.dialog_kbar_shadow:       return new KbarShadow(dialog);
            case R.layout.dialog_kbar_extremum:     return new KbarExtremum(dialog);
            case R.layout.dialog_kd_d:              return new KDD(dialog);
            case R.layout.dialog_kd_k:              return new KDK(dialog);
            case R.layout.dialog_macd_bar:          return new MACDBar(dialog);
            case R.layout.dialog_rsi_val:           return new RSIVal(dialog);
            case R.layout.dialog_volume_continue:   return new VolumeContinue(dialog);
            case R.layout.dialog_volume_ma:         return new VolumeMA(dialog);
            case R.layout.dialog_volume_surge:         return new VolumeSurge(dialog);
            case R.layout.dialog_bias_val:          return new BIASVal(dialog);
            case R.layout.dialog_bband_top:  return new BbandTouchTop(dialog);
            case R.layout.dialog_bband_bottom:  return new BbandTouchBottom(dialog);
            case R.layout.dialog_ma_death_cross:    return new MADeathCross(dialog);
            case R.layout.dialog_ma_golden_cross:   return new MAGoldenCross(dialog);
            case R.layout.dialog_rsi_death_cross:   return new RSIDeathCross(dialog);
            case R.layout.dialog_rsi_golden_cross:  return new RSIGoldenCross(dialog);
            case R.layout.dialog_kd_death_cross:    return new KDDeathCross();
            case R.layout.dialog_kd_golden_cross:   return new KDGoldenCross();
            case R.layout.dialog_macd_death_cross:  return new MACDDeathCross();
            case R.layout.dialog_macd_golden_cross: return new MACDGoldenCross();
            default:
                return null;

        }
    }
}

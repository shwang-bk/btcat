package studio.bluekitten.backtestingcat.components.target;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

import studio.bluekitten.backtestingcat.R;
import studio.bluekitten.backtestingcat.util.AdRequestGenerator;

/**
 * Created by bluecat on 2017/2/11.
 */

public class ProgressControlTask extends AsyncTask<Void, Integer, Void> {
    private int step, progress;
    private boolean interrupt, isADShowing;
    private Context context;
    private ProgressDialog dialog;
    InterstitialAd interstitial;

    public ProgressControlTask(Context context){
        this.context = context;
        this.interrupt = false;
        this.isADShowing = false;
        initInsertAd();
    }

    public void initInsertAd(){
        interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(context.getString(R.string.interstitial_ad_unit_id));
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                isADShowing = false;
            }
        });

        AdRequest adRequest = AdRequestGenerator.getNewAdRequest();
        interstitial.loadAd(adRequest);
    }

    @Override
    protected void onPreExecute(){
        generateDialog();
        dialog.setButton(ProgressDialog.BUTTON_NEUTRAL, "看廣告", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(interstitial.isLoaded()) {
                    interstitial.show();
                    isADShowing = true;
                }
                generateDialog();
                dialog.show();
            }
        });
        dialog.show();
    }

    private void generateDialog(){
        dialog = new ProgressDialog(context);
        dialog.setMessage("數據計算中...\n無聊的話可以看段廣告喔!");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Random random = new Random();
        try {
            while(progress < 100 && !interrupt) {
                if(progress < step) {
                    Thread.sleep(500);
                    progress += random.nextInt(13);
                    publishProgress(Integer.valueOf(progress));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(isADShowing);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        dialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Void result){
        dialog.dismiss();
    }

    public void setStep(int i){
        step = i;
    }

    public void interrupt(boolean interrupt){
        this.interrupt = interrupt;
    }
}

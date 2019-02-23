package studio.bluekitten.backtestingcat.util;

import com.google.android.gms.ads.AdRequest;

import java.util.HashSet;

public class AdRequestGenerator {
    public static AdRequest getNewAdRequest(){
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("F70493B7B9FEB51C050D51D5BBCAA5FF")
                .addTestDevice("044CCF8337598F3D664DB89B03D17916")
                .build();
        return adRequest;
    }
}

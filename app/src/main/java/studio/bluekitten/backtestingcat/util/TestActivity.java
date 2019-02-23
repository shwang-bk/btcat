package studio.bluekitten.backtestingcat.util;

// 為測試用 Activity，不會產生任何畫面

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import studio.bluekitten.backtestingcat.R;

public class TestActivity extends AppCompatActivity {
    public static final String TEST_LOG = "TEST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

    }

}

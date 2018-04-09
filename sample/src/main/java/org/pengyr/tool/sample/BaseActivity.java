package org.pengyr.tool.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * 所有Activity的父類別
 * <p>
 * Created by Peng on 2017/10/29.
 */
public class BaseActivity extends AppCompatActivity {

    private final static String TAG = BaseActivity.class.getSimpleName();

    // activity is alive or not
    protected boolean isAlive;

    // activity is shown on foreground or not
    protected boolean isShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAlive = true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        isShown = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isShown = false;
    }

    @Override
    protected void onDestroy() {
        isAlive = false;
        super.onDestroy();
    }

    public Activity getActivity() {
        return this;
    }

}

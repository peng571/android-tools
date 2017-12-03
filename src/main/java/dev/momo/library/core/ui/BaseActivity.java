package dev.momo.library.core.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import dev.momo.library.core.Helper;
import dev.momo.library.core.tool.ResourceHelper;

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

        // TODO StrictMode.setThreadPolicy
        //            StrictMode.setThreadPolicy(
        //                    new StrictMode.ThreadPolicy.Builder()
        //                            .detectDiskReads()
        //                            .detectDiskWrites()
        //                            .detectAll()
        //                            .penaltyLog()
        //                            .build());
        //            StrictMode.setVmPolicy(
        //                    new StrictMode.VmPolicy.Builder()
        //                            .detectLeakedSqlLiteObjects()
        //                            .detectLeakedClosableObjects()
        //                            .penaltyLog()
        //                            .penaltyDeath()
        //                            .build());

        // TODO Orientation setting
        //        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // TODO crash exception handler
        //        Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler(this));


        // TODO logger settup
        //        Logger.setTAG(Constants.TAG);
        //        if (!ProductMode.DEBUG) {
        //            Logger.setLogLevel(Logger.LOG_NONE);
        //        }

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

    /**
     * Change status bar methods
     */
    @TargetApi(21)
    public void setStatusBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ResourceHelper.getColor(color));
    }


    public void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


    // TODO TBD...
    interface OrientationConfig {

        int ORIENTATION_PORTRAOT = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        void getOrientationOnly();
    }

}

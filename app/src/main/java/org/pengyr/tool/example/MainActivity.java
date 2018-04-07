package org.pengyr.tool.example;

import android.app.Activity;
import android.os.Bundle;

import org.pengyr.tool.core.AppHelper;
import org.pengyr.tool.core.log.Logger;
import org.pengyr.tool.core.tool.PreferenceHelper;

/**
 * Created by Peng on 2018/3/25.
 */
public class MainActivity extends Activity {

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // init app helper at entry activity or application
        AppHelper.initInstance(getApplicationContext());

        // then, you can use other support like logger
        String globalTag = "Example";
        Logger.setTAG(globalTag);
        Logger.setLogLevel(Logger.LOG_ALL);
        Logger.I(TAG, "app has init");
        // you can see new log with `@globaleTag`_`@TAG` add on Logcat


        PreferenceHelper.set("key", "value");
//        Logger.D(TAG, "get preference key %s", PreferenceHelper.getString("key", ""));
//        Logger.D(TAG, "get app name %s from res", ResourceHelper.getString(R.string.app_name));
    }
}

package org.pengyr.tool.sample;

import android.os.Bundle;
import android.widget.TextView;

import org.pengyr.tool.core.AppHelper;
import org.pengyr.tool.core.log.Logger;
import org.pengyr.tool.core.tool.PreferenceHelper;
import org.pengyr.tool.core.tool.ResourceHelper;
import org.pengyr.tool.core.util.ThreadUtil;

/**
 * Created by Peng on 2018/3/25.
 */
public class MainActivity extends BaseActivity {

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
        // you can see new log with `@globaleTag`_`@TAG` add on Logcat
        Logger.I(TAG, "app has init");


        PreferenceHelper.set("key", "value");
        Logger.D(TAG, "get preference key %s", PreferenceHelper.getString("key", ""));
        Logger.D(TAG, "get app name %s from res", ResourceHelper.getString(R.string.app_name));

        // start new thread in main loop
        ThreadUtil.mainUI(() -> reloadUI());
    }

    private void reloadUI() {
        TextView textView = findViewById(R.id.textView);
        String message = ResourceHelper.getString(R.string.build_success_message, AppHelper.getVersion());
        Logger.D(TAG, "get message %s", message);
        textView.setText(message);
    }
}

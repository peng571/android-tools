package org.pengyr.tool.core;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import org.pengyr.tool.core.tool.PreferenceHelper;
import org.pengyr.tool.core.tool.ResourceHelper;
import org.pengyr.tool.core.util.NetworkUtil;
import org.pengyr.tool.core.util.ThreadUtil;
import org.pengyr.tool.core.view.DisplayHelper;


/**
 * Must call Helper.init(context) at first
 */
public class AppHelper {

    private final static String TAG = AppHelper.class.getSimpleName();

    private static AppHelper instance;
    private static Context applicationContext;

    private AppHelper(@NonNull Context appContext) {
        applicationContext = appContext.getApplicationContext();
        ResourceHelper.init(applicationContext, appContext.getPackageName());
        PreferenceHelper.init(applicationContext, appContext.getPackageName());
        DisplayHelper.init(applicationContext);
        ThreadUtil.init(applicationContext);
        NetworkUtil.init(applicationContext);
    }

    public static AppHelper initInstance(@NonNull Context appContext) {
        if (instance == null) {
            synchronized (AppHelper.class) {
                if (instance == null) {
                    instance = new AppHelper(appContext);
                }
            }
        }
        return instance;
    }


    public static void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    private static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }


    public static String getVersion() {
        try {
            return applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "1.0";
        }
    }

    public static void release() {
        ResourceHelper.release();
        PreferenceHelper.release();
        applicationContext = null;
    }

}

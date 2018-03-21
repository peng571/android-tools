package dev.momo.library.core.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;

/**
 * Created by momo peng on 2018/3/21.
 */

public class NetworkUtil {

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

}

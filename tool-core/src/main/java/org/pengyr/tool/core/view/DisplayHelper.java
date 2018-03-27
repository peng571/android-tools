package org.pengyr.tool.core.view;

/**
 * Created by Peng on 2015/12/26.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;

import java.util.Locale;

import org.pengyr.tool.core.log.Logger;

public class DisplayHelper {

    private final static String TAG = DisplayHelper.class.getSimpleName();

    public static int DEVICE_HEIGHT = 0;

    public static int SCREEN_DPI = 0;
    public static float SCREEN_DENSITY = 0.0f;
    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;


    public static void init(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        SCREEN_WIDTH = displayMetrics.widthPixels;
        DEVICE_HEIGHT = displayMetrics.heightPixels;
        SCREEN_HEIGHT = DEVICE_HEIGHT - getNavigationHeight(context) - getStatusBarHeight(context);
        Logger.I(TAG, "DEVICE_HEIGHT %d - getNavigationHeight(context) %d - getStatusBarHeight(context) %d = %d",
                 displayMetrics.heightPixels, getNavigationHeight(context), getStatusBarHeight(context), SCREEN_HEIGHT);
        SCREEN_DENSITY = displayMetrics.density;
        SCREEN_DPI = displayMetrics.densityDpi;
    }


    private static int getNavigationHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    public static int dpToPixel(int dp) {
        return (int) ((float) dp * SCREEN_DENSITY + 0.5f);
    }

    public static int dpToPixel(float dp) {
        return (int) (dp * SCREEN_DENSITY + 0.5f);
    }

    public static int pixelToDp(float px) {
        return (int) (px / SCREEN_DENSITY + 0.5f);
    }

    public static Point getScreenMetrics() {
        return new Point(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public static float getScreenRatio() {
        return ((float) SCREEN_HEIGHT / SCREEN_WIDTH);
    }

    public static String getInfoMessage() {
        return String.format(Locale.getDefault(), "Screen W:%d, H:%d, Ratio(H/W)%.2f", SCREEN_WIDTH, SCREEN_HEIGHT, getScreenRatio());
    }
}
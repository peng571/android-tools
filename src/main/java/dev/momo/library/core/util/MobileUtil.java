package dev.momo.library.core.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;

import dev.momo.library.core.view.DisplayHelper;

/**
 * Created by momo peng on 2018/3/21.
 */

public class MobileUtil {

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        return getDeviceBrand() + getDeviceModel();
    }

    @TargetApi(19)
    public static String getDeviceBrand() {
        return Build.MANUFACTURER.toLowerCase();
    }

    public static String getDeviceModel() {
        String model = Build.MODEL;
        if (model.startsWith(getDeviceBrand())) {
            model = model.substring(getDeviceBrand().length());
        }
        return capitalize(model);
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }
        return phrase.toString();
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isPad() {
        if (((float) DisplayHelper.SCREEN_WIDTH / DisplayHelper.SCREEN_DENSITY) > 400.0f) {
            return true;
        } else {
            return false;
        }
    }

}

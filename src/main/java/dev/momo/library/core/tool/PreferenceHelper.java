package dev.momo.library.core.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Peng on 2015/12/3.
 */
public class PreferenceHelper {

    private static SharedPreferences preferences = null;


    public static void init(Context context, String packageName) {
        preferences = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
    }

    public static void set(String key, int value) {
        if (preferences == null) {
            return;
        }
        preferences.edit().putInt(key, value).apply();
    }


    public static void set(String key, String value) {
        if (preferences == null) {
            return;
        }
        preferences.edit().putString(key, value).apply();
    }

    public static void set(String key, boolean value) {
        if (preferences == null) {
            return;
        }
        preferences.edit().putBoolean(key, value).apply();
    }

    public static void set(String key, float value) {
        if (preferences == null) {
            return;
        }
        preferences.edit().putFloat(key, value).apply();
    }


    public static void set(String key, double value) {
        if (preferences == null) {
            return;
        }
        preferences.edit().putLong(key, Double.doubleToRawLongBits(value)).apply();
    }

    // get this when need editing huge data
    public static SharedPreferences.Editor get() {
        if (preferences == null) {
            return null;
        }

        return preferences.edit();
    }

    public static int getInt(String key, int defaultValue) {
        if (preferences == null) {
            return defaultValue;
        }
        return preferences.getInt(key, defaultValue);
    }

    public static float getFloat(String key, float defaultValue) {
        if (preferences == null) {
            return defaultValue;
        }
        return preferences.getFloat(key, defaultValue);
    }


    public static boolean getBoolean(String key, boolean defaultValue) {
        if (preferences == null) {
            return defaultValue;
        }
        return preferences.getBoolean(key, defaultValue);
    }

    public static String getString(String key, String defaultValue) {
        if (preferences == null) {
            return defaultValue;
        }
        return preferences.getString(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        if (preferences == null) {
            return defaultValue;
        }
        return preferences.getLong(key, defaultValue);
    }


    public static double getDouble(String key, double defaultValue) {
        if (preferences == null) {
            return defaultValue;
        }

        if (!preferences.contains(key)) {
            return defaultValue;
        }

        return Double.longBitsToDouble(preferences.getLong(key, 0));
    }

}

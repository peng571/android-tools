package org.pengyr.tool.core.tool;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import org.pengyr.tool.core.log.Logger;

import java.util.IllegalFormatConversionException;

/**
 * Created by Peng on 2015/12/3.
 */
public class ResourceHelper {

    private final static String TAG = ResourceHelper.class.getSimpleName();

    public static final String RESOURCE_TYPE_ID = "id";
    public static final String RESOURCE_TYPE_RAW = "raw";
    public static final String RESOURCE_TYPE_DRAWABLE = "drawable";
    public static final String RESOURCE_TYPE_String = "string";

    private static Context applicationContext;
    private static String appPackageName;
    public static Resources resources;

    public static void init(Context context, String packageName) {
        applicationContext = context.getApplicationContext();
        resources = applicationContext.getResources();
        appPackageName = packageName;

    }

    public static Resources get() {
        return resources;
    }

    public static int getID(String name) {
        return getID(RESOURCE_TYPE_ID, name);
    }

    public static int getID(String type, String name) {
        try {
            int res = applicationContext.getResources().getIdentifier(name, type, appPackageName);
            return res;
        } catch (Resources.NotFoundException e) {
            Logger.E(TAG, e);
            return 0;
        }
    }

    public static int getColor(@ColorRes int resId) {
        if (resources == null) return Color.BLACK;
        if (resId == 0) return Color.BLACK;
        try {
            return ContextCompat.getColor(applicationContext, resId);
        } catch (Resources.NotFoundException e) {
            Logger.E(TAG, e);
            return Color.BLACK;
        }
    }

    @NonNull
    public static String getString(@StringRes int resId) {
        if (resources == null) return "";
        if (resId == 0) return "";
        try {
            return resources.getString(resId);
        } catch (Resources.NotFoundException e) {
            Logger.E(TAG, e);
            return "";
        }

    }

    @NonNull
    public static String getString(@StringRes int resId, Object... formatArgs) {
        if (resources == null) return "";
        if (resId == 0) return "";
        try {
            return resources.getString(resId, formatArgs);
        } catch (IllegalFormatConversionException e) {
            Logger.E(TAG, e);
        }
        return "";
    }

    public static String[] getStringArray(@ArrayRes int resId) {
        String[] strings = new String[]{""};
        if (resources == null) return strings;
        if (resId == 0) return strings;
        try {
            strings = resources.getStringArray(resId);
        } catch (Resources.NotFoundException e) {
            Logger.E(TAG, e);
        }
        return strings;
    }

    public static String getStringInArray(@ArrayRes int resID, int index) {
        String[] strings = getStringArray(resID);
        if (strings == null || strings.length <= index) return "";
        return strings[index];
    }

    public static @StringRes int getStringID(@ArrayRes int resID, int index) {
        if (resources == null) return 0;
        TypedArray array = null;
        try {
            array = resources.obtainTypedArray(resID);
            return array.getResourceId(index, 0);
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    public static int getDimenPixel(@DimenRes int resId) {
        if (resources == null) return 0;
        return (int) resources.getDimension(resId);
    }

    public static int getDimenDp(@DimenRes int resId) {
        if (resources == null) return 0;
        return Math.round(getDimenPixel(resId) / resources.getDisplayMetrics().density);
    }

    public static float getDimenFloat(@DimenRes int resId) {
        TypedValue outValue = new TypedValue();
        resources.getValue(resId, outValue, true);
        return outValue.getFloat();
    }

    public static void release() {
        resources = null;
        applicationContext = null;
        appPackageName = null;
    }

}
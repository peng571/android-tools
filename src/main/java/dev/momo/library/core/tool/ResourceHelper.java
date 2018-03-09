package dev.momo.library.core.tool;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import java.util.IllegalFormatConversionException;

import dev.momo.library.core.log.Logger;

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
            //            Logger.E("CAN NOT FIND RES WITH ID " + name);
            Logger.E(TAG, e);
            return 0;
        }
    }

    public static int getColor(@ColorRes int resId) {
        if (resources == null) return Color.BLACK;
        if (resId == 0) return Color.BLACK;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return ContextCompat.getColor(applicationContext, resId);
            }
            return resources.getColor(resId);
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

    public static String getString(@ArrayRes int resID, int index) {
        String[] strings = getStringArray(resID);
        if (strings == null || strings.length <= index) return "";
        return strings[index];
    }

    public static @StringRes int getStringID(@ArrayRes int resID, int index) {
        if (resources == null) return 0;
        TypedArray array = resources.obtainTypedArray(resID);
        return array.getResourceId(index, 0);
    }

    @Nullable
    public static Drawable getDrawable(@DrawableRes int resId) {
        if (resources == null) return null;
        if (resId == 0) return null;
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                return resources.getDrawable(resId, applicationContext.getTheme());
            } else {
                return resources.getDrawable(resId);
            }
        } catch (Resources.NotFoundException e) {
            Logger.E(TAG, e);
            return null;
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
}
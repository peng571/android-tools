package dev.momo.library.core.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import dev.momo.library.core.log.Logger;

public class KeyboardUtil {

    private static final String TAG = KeyboardUtil.class.getSimpleName();


    public static void hideKeyboard(Activity activity) {
        if (activity == null) {
            Logger.D(TAG, "activity is null");
            return;
        }
        if (activity.getWindow() == null) {
            Logger.D(TAG, "activity get null window");
            return;
        }
        View view = activity.getWindow().getCurrentFocus();
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view == null) {
            Logger.WS(TAG, "hide keyboard failed, course without current focus token");
            return;
        }
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void hideKeyboard(Activity activity, View currentExistView) {
        if (activity == null) {
            Logger.D(TAG, "activity is null");
            return;
        }
        if (currentExistView == null) {
            Logger.WS(TAG, "hide keyboard failed, current exist view is null");
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(currentExistView.getWindowToken(), 0);
    }

    public static void hideKeyboard(final Activity activity, long delayMillis) {
        ThreadUtil.mainUI(() -> hideKeyboard(activity), delayMillis);
    }


    public static void showKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void showKeyboard(final Context context, long delayMillis) {
        ThreadUtil.mainUI(() -> showKeyboard(context), delayMillis);
    }


    /**
     * For Dialog
     */


    public static void showKeyboard(final Dialog dialog, long delayMillis) {
        ThreadUtil.mainUI(() -> showKeyboard(dialog), delayMillis);
    }


    public static void showKeyboard(Dialog dialog) {
        Logger.D(TAG, "showKeyboard on dialog");
        if (dialog == null) {
            Logger.WS(TAG, "dialog is null");
            return;
        }

        if (dialog.getWindow() == null) {
            Logger.WS(TAG, "show keyboard failed, dialog's window is null");
            return;
        }
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public static void hideKeyboard(Dialog dialog) {
        if (dialog == null) {
            Logger.WS(TAG, "dialog is null");
            return;
        }

        if (dialog.getWindow() == null) {
            Logger.WS(TAG, "hide keyboard failed, dialog's window is null");
            return;
        }
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}

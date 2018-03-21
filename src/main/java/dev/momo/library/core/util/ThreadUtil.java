package dev.momo.library.core.util;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;


/**
 * Must call Helper.init(...) at first
 */
public class ThreadUtil {

    private final static String TAG = ThreadUtil.class.getSimpleName();

    private final static String THREAD_NAME = "BACKGROUND_THREAD";

    /* Threads */
    private static Handler background;
    private static Handler mainUI;

    private ThreadUtil(@NonNull Context appContext) {
        HandlerThread backgroundHandlerThread = new HandlerThread(THREAD_NAME);
        backgroundHandlerThread.start();
        background = new Handler(backgroundHandlerThread.getLooper());
        mainUI = new Handler(appContext.getMainLooper());
    }

    public static void background(Runnable runnable) {
        background.post(runnable);
    }

    public static void background(Runnable runnable, long delay) {
        background.postDelayed(runnable, delay);
    }

    public static void mainUI(Runnable runnable) {
        mainUI.post(runnable);
    }

    public static void mainUI(Runnable runnable, long delay) {
        mainUI.postDelayed(runnable, delay);
    }
}

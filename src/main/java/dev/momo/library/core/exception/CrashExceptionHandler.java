//package dev.momo.library.core.exception;
//
//import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//import com.alchema.app.ui.page.MainActivity;
//import com.google.firebase.crash.FirebaseCrash;
//
//import dev.momo.library.core.Helper;
//import dev.momo.library.core.log.Logger;
//
///**
// * To handle all exception that cause crash
// * <p>
// * do restart application and entry from MainActivity
// * <p>
// * Created by momo peng on 2017/10/5.
// */
//
//public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {
//
//    private final static String TAG = CrashExceptionHandler.class.getSimpleName();
//
//    private Activity activity;
//
//    public CrashExceptionHandler(Activity a) {
//        activity = a;
//    }
//
//    @Override
//    public void uncaughtException(Thread thread, Throwable ex) {
//        Logger.ES(TAG, "catch crash exception");
//        Logger.E(TAG, ex);
//        FirebaseCrash.logcat(Log.ERROR, TAG, "crash exception caught");
//        FirebaseCrash.report(ex);
//
//        Intent intent = new Intent(activity, Helper.getContext());
//        intent.putExtra("crash", true);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                | Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(AlchemaApplication.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager mgr = (AlarmManager) AlchemaApplication.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
//        activity.finish();
//        System.exit(2);
//
//        Logger.ES(TAG, "restart application");
//    }
//
//    interface crashListener {
//        void onCrash();
//    }
//}

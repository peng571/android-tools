package dev.momo.library.core;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import dev.momo.library.core.log.Logger;
import dev.momo.library.core.tool.PreferenceHelper;
import dev.momo.library.core.tool.ResourceHelper;
import dev.momo.library.core.view.DisplayHelper;

import static dev.momo.library.core.Constants.REQUEST_CHECK_EMAIL;


/**
 * Must call Helper.init(...) at first
 */
public class Helper {

    private final static String TAG = Helper.class.getSimpleName();

    /* Threads */
    public static HandlerThread backgroundHandlerThread;
    public static Handler backgroundHandler;
    public static Handler mainUIHandler;

    public static Handler handler;

    private static ContextWrapper contextWrapper = null;
    private static Context applicationContext = null;
    private static Helper helper;


    private Helper(@NonNull Context appContext) {
        backgroundHandlerThread = new HandlerThread("DB_THREAD");
        backgroundHandlerThread.start();
        backgroundHandler = new Handler(backgroundHandlerThread.getLooper());
        mainUIHandler = new Handler(appContext.getMainLooper());


        if (applicationContext == null) {
            applicationContext = appContext.getApplicationContext();
            contextWrapper = new ContextWrapper(applicationContext);
            handler = new Handler(appContext.getMainLooper());
            ResourceHelper.init(applicationContext, appContext.getPackageName());
            PreferenceHelper.init(applicationContext, appContext.getPackageName());
            DisplayHelper.init(appContext);
        }
    }

    public static Context getContext() {
        return applicationContext;
    }


    public static Helper initInstance(@NonNull Context appContext) {
        if (helper == null) {
            synchronized (Helper.class) {
                if (helper == null) {
                    helper = new Helper(appContext);
                }
            }
        }
        return helper;
    }


    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    @TargetApi(19)
    protected static String getRealPathFromURIKitKat(Uri uri, Context context) {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        return null;
    }


    public static String getRealPathFromURI(Uri uri, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return getRealPathFromURIKitKat(uri, context);
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };


        String fileColumn = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                fileColumn = cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        Logger.D(TAG, "file path : " + fileColumn);
        return fileColumn;
    }

    // FIXME add db promission
    //    @TargetApi(23)
    //    private void checkPermission(Context context) {
    //        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
    //            return;
    //        }
    //
    //        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    //                != PackageManager.PERMISSION_GRANTED) {
    //
    //            // Should we show an explanation?
    //            if (context.shouldShowRequestPermissionRationale(
    //                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
    //                // Explain to the user why we need to read the contacts
    //            }
    //
    //            context.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
    //                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    //
    //            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
    //            // app-defined int constant that should be quite unique
    //
    //            return;
    //        }
    //
    //    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static Date parseDate(String format, String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            Logger.E(TAG, e);
        }
        return Calendar.getInstance().getTime();
    }

    public static String formatDate(String format, long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(timestamp);
    }

    public static String formatDate(String format, int timestamp) {
        return formatDate(format, timestamp * 1000L);
    }

    public static String toElapsedTimeString(long timestamp) {
        return toElapsedTimeString((int) (timestamp / 1000));
    }

    public static String toElapsedTimeString(int timestamp) {
        int currentTime = getCurrentTimestamp();
        int timeOffset = currentTime - timestamp;
        if (timeOffset < 0) {
            // ?
            return ResourceHelper.getString(R.string.time_just_now);
        } else if (timeOffset <= 60) { // 一分鐘內
            return ResourceHelper.getString(R.string.time_just_now);
        } else if (timeOffset <= 3600) { // 一小時內
            return ResourceHelper.getString(R.string.time_minutes_of, timeOffset / 60);
        } else if (timeOffset < 3600 * 24) { // 一天內
            return ResourceHelper.getString(R.string.time_hours_of, timeOffset / 3600);
            //        } else if (timeOffset < 86400) { // 一天內
            //            return formatDate(DATEFORMAT_HM, timeOffset);
        } else {
            return ResourceHelper.getString(R.string.time_days_of, timeOffset / 86400);
        }
    }

    public static int getCurrentTimestamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        return (int) (calendar.getTimeInMillis() / 1000L);
    }


    public static String getUUIDFileName(String fileExtension) {
        return getUUID() + "." + fileExtension;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getRandomString(int length) {
        return getRandomString(length, "0123456789" +
                "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    public static String getRandomNumberString(int length) {
        return getRandomString(length, "0123456789");
    }

    public static String getRandomString(int length, String cell) {
        String randomString = "";
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            int randNum = rand.nextInt(cell.length() - 1);
            randomString += cell.charAt(randNum);
        }
        return randomString;
    }


    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Pattern
                    .compile(
                            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+").matcher(target).matches();
        }
    }


    /**
     * Send with request @See Constans.REQUEST_CHECK_EMAIL (1001)
     *
     * @param activity will result at activity.onActivityResult
     */
    public static void openDefaultEmapApp(Activity activity) {
        // open default email application
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        //        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(intent, REQUEST_CHECK_EMAIL);
    }

    public static void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    public static long dirSize(File dir) {
        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // Recursive call if it's a directory
                if (fileList[i].isDirectory()) {
                    result += dirSize(fileList[i]);
                } else {
                    // Sum the file size in bytes
                    result += fileList[i].length();
                }
            }
            return result; // return the file size
        }
        return 0;
    }

    public static boolean isPad() {
        if (((float) DisplayHelper.SCREEN_WIDTH / DisplayHelper.SCREEN_DENSITY) > 400.0f) {
            return true;
        } else {
            return false;
        }
    }


    private static boolean isAppInstalled(String packageName) {
        PackageManager pm = applicationContext.getPackageManager();
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

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public static String fileNameFromUrl(String url, String fileType) {
        try {
            return SHA1(url) + "." + fileType;
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isTablet() {
        return (applicationContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void cleanUpMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public static String getDeviceID() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
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

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(source);
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(@StringRes int sourceRes) {
        return fromHtml(ResourceHelper.getString(sourceRes));
    }
}

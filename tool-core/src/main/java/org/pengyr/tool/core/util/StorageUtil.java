package org.pengyr.tool.core.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.pengyr.tool.core.log.Logger;

/**
 * Storage Rule:
 * <p/>
 * Storage at:
 * - 相簿:  公開的拍照檔，不隨程式移除而消失
 * - 相簿內專案資料夾: 公開的特定類型圖片檔，不隨程式移除而消失
 * - 內部專案資料夾: 用戶可看到，會隨著程式移除而消失
 * <p/>
 * <p/>
 * File Type With Folder
 * - Movie - Environment.DIRECTORY_MOVIES
 * - Picture - Environment.DIRECTORY_PICTURES
 * - File: db
 * - Temp
 * <p/>
 * All path will be replace '\\' into '/'
 * <p/>
 */
public class StorageUtil {

    private static final String TAG = StorageUtil.class.getSimpleName();

    public static boolean fileExist(String filePath) {
        return new File(filePath).exists();
    }

    public static long getFileSize(String filePath) {
        return new File(filePath).length();
    }


    /* check if external sdcard is mounted */
    public static boolean externalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
            return true;
        else
            return false;
    }

    public static boolean isSdCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    public static void copyFile(File sourceFile, File destFile) {
        FileChannel source = null;
        FileChannel destination = null;
        try {
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        } catch (Exception e) {
            destFile.delete();
            Logger.E("COPY FILE EXCEPTION: ", e);
        }
    }


    public static File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
            Logger.E(TAG, e);
        }
        return file;
    }


    /**
     * @return /Android/data/{packagename}/files/
     */
    public static File getAppFilesDir(Context context) {
        return context.getFilesDir();
    }


    /**
     * @return the directory for the user's public pictures directory.
     */
    public static File getPublicMovieStorageDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }

    public static File getPublicMovieStorageDir(String folder) {
        File f = new File(join(getPublicAlbumStorageDir(), folder));
        if (!f.exists()) {
            if (!f.mkdir()) {
                Logger.ES(TAG, "create file error");
                return null;
            }
        }
        return f;
    }

    /**
     * @return the directory for the user's public pictures directory.
     */
    public static File getPublicAlbumStorageDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }


    /**
     * @param context
     * @return the directory for the app's private pictures directory.
     */
    public static File getAlbumStorageDir(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }


    /**
     * Checks if external storage is available for read and write
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * Checks if external storage is available to at least read
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }


    public static String getFilePath(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }

        File file = new File(filePath);
        if (file.isDirectory()) {
            return filePath;
        }
        return file.getParentFile().getPath();
    }

    public static String getFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }

        File file = new File(filePath);
        if (file.isFile()) {
            return file.getName();
        }
        return "";

        //        if (filePath == null || filePath.isEmpty()) {
        //            return "";
        //        }
        //        filePath = filePath.replaceAll("\\\\", "/");
        //        String[] components = filePath.split("/");
        //        return components[components.length - 1];
    }

    public static String join(String parentFile, String currentFileName) {
        parentFile = parentFile.replaceAll("\\\\", "/");
        if (!parentFile.endsWith("/")) {
            parentFile += "/";
        }
        return parentFile + currentFileName;
    }

    public static String join(File parentFile, String currentFileName) {
        String realPath;
        try {
            realPath = parentFile.getCanonicalPath();
        } catch (IOException e) {
            Logger.E(TAG, e);
            realPath = parentFile.getPath();
        }
        return join(realPath, currentFileName);
    }

    public static String join(File parentFile, String... joinFile) {
        String path = parentFile.getPath();
        for (String s : joinFile) {
            path = join(path, s);
        }
        return path;
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

}

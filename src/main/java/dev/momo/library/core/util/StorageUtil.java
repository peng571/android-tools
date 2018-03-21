package dev.momo.library.core.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import dev.momo.library.core.log.Logger;

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


}

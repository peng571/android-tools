package org.pengyr.tool.core.image;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.io.File;

import org.pengyr.tool.core.AppHelper;
import org.pengyr.tool.core.log.Logger;
import org.pengyr.tool.core.util.StorageUtil;


/**
 * Helper for get Photo
 */
public class PhotoPicker {

    private final static String TAG = PhotoPicker.class.getSimpleName();

    private final static int REQUEST_STORAGE_CODE = 124;


    private Activity resultActivity;
    private Fragment resultFragment;
    boolean isFromFragment;

    private String filePath = "";

    private OnPhotoPickListener listener;


    public PhotoPicker(Activity resultActivity) {
        this.resultActivity = resultActivity;
        this.isFromFragment = false;
    }

    public PhotoPicker(Fragment resultFragment) {
        this.resultFragment = resultFragment;
        this.resultActivity = resultFragment.getActivity();
        this.isFromFragment = true;
    }

    public boolean isRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public boolean checkPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(PERMISSIONS, REQUEST_STORAGE_CODE);
                return false;
            }
        }
        return true;
    }


    public void handleOnPhotoPickerResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageConfig.TAKE_PICTURE_REQUEST:
                filePath = getExternalTempImagePath(resultActivity);
                break;
            case ImageConfig.GALLERY_INTENT_REQUEST:
                if (data != null && data.getData() != null) {
                    filePath = StorageUtil.getRealPathFromURI(data.getData(), resultActivity);
                } else {
                    Logger.ES(TAG, "Pick Image Error!");
                }
                break;
            default:
                return;
        }

        if (filePath == null || filePath.isEmpty()) {
            Logger.ES(TAG, "get photo failed");
            return;
        }
        Logger.D(TAG, "getRealPathFromURI filePath: " + filePath);
        if (listener != null) {
            listener.onPhotoPick(filePath);
        }
    }


    private static Intent getPickImageIntent() {
        Intent intent = new Intent();

        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");

        return intent;
    }


    public static void startPickImageIntent(Activity activity) {
        activity.startActivityForResult(getPickImageIntent(), ImageConfig.GALLERY_INTENT_REQUEST);
    }

    public static void startPickImageIntent(Fragment fragment) {
        fragment.startActivityForResult(getPickImageIntent(), ImageConfig.GALLERY_INTENT_REQUEST);
    }


    private static Intent getTakePictureIntent(Context context) {
        new File(getExternalTempImagePath(context)).delete();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getExternalTempImagePath(context))));
        return intent;
    }


    public static void startTakePictureIntent(Activity activity) {
        activity.startActivityForResult(getTakePictureIntent(activity), ImageConfig.TAKE_PICTURE_REQUEST);
    }

    public static void startTakePictureIntent(Fragment fragment) {
        fragment.startActivityForResult(getTakePictureIntent(fragment.getActivity()), ImageConfig.TAKE_PICTURE_REQUEST);
    }

    public PhotoPicker setOnPhotoPickListener(OnPhotoPickListener onPhotoPickListener) {
        this.listener = onPhotoPickListener;
        return this;
    }


    public interface OnPhotoPickListener {
        void onPhotoPick(String filePath);
    }


    private static String getExternalMediaFolderPath(Context context) {
        return StorageUtil.getAlbumStorageDir(context).toString();
    }


    private static String getExternalTempImagePath(Context context) {
        return StorageUtil.join(getExternalMediaFolderPath(context), "tmp.jpg");
    }

}
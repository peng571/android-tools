package dev.momo.library.core.image;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dev.momo.library.core.log.Logger;
import dev.momo.library.core.util.StorageUtil;


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

    /**
     * @return PictureFileName
     */
    public PhotoPicker(Activity resultActivity) {
        this.resultActivity = resultActivity;
        this.isFromFragment = false;
    }

    public PhotoPicker(Fragment resultFragment) {
        this.resultFragment = resultFragment;
        this.resultActivity = resultFragment.getActivity();
        this.isFromFragment = true;
    }


    //    public void showPickerOptions() {
    //        showPickerOptions(resultActivity);
    //    }


    public boolean isRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public boolean checkPermission(Activity activity) {
        Logger.D(TAG, "check permission");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS = {
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
            };
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(PERMISSIONS, REQUEST_STORAGE_CODE);
                Logger.D(TAG, "check permission return false");
                return false;
            }
        }
        Logger.D(TAG, "check permission return true");
        return true;
    }


    public void handleOnPhotoPickerResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageConfig.TAKE_PICTURE_REQUEST:
                filePath = getExternalTempImagePath(resultActivity);
                break;
            case ImageConfig.GALLERY_INTENT_REQUEST:
                if (data != null && data.getData() != null) {
//                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        //                                             will return file
//                        filePath = StorageUtil.getRealPathFromURI(data.getExtras().g)
//                    } else {
                        // will return uri only
                        filePath = StorageUtil.getRealPathFromURI(data.getData(), resultActivity);
//                    }
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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
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


    @TargetApi(22) private static Intent getTakePictureIntent22(Context context) {
        new File(getExternalTempImagePath(context)).delete();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getExternalTempImagePath(context))));
        return intent;
    }

    @TargetApi(24) private static Intent getTakePictureIntent24(Activity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(activity);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Logger.E(TAG, ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        "com.alchema.app.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
        }
        return takePictureIntent;
    }


    private static Intent getTakePictureIntent(Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return getTakePictureIntent24(activity);
        } else {
            return getTakePictureIntent22(activity);
        }
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


    private static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        String capturePhotoPath = image.getAbsolutePath();
        Logger.D(TAG, "new photo path %s", capturePhotoPath);
        return image;
    }

    public void clear() {
        this.resultFragment = null;
        this.resultActivity = null;
    }

}
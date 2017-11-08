package dev.momo.library.core.image;

import android.graphics.Bitmap;

/**
 * Created by Peng on 2016/6/17.
 */
public class ImageConfig {

    /* Activity Request */ /* Request Codes */
    public static final int GALLERY_INTENT_REQUEST = 19621;
    public static final int TAKE_PICTURE_REQUEST = 19622;

    // 壓縮品質
    public final static int COMPRESS_RATIO = 100;
    public final static Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

}

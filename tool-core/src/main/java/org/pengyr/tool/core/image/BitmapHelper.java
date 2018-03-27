package org.pengyr.tool.core.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.pengyr.tool.core.log.Logger;

public class BitmapHelper {

    private final static String TAG = BitmapHelper.class.getSimpleName();

    /**
     * Create Bitmap
     *
     * @param w     width of Bitmap size
     * @param h     height of Bitmap size
     * @param color
     * @return new Bitmap
     */
    public static Bitmap createBitmap(int w, int h, @ColorInt int color) {
        int stride = getBaseStride(w);
        int[] colors = createColors(color, stride * h);
        Bitmap b = Bitmap.createBitmap(colors, 0, stride, w, h, ImageConfig.BITMAP_CONFIG);
        //        Bitmap b2 = BitmapHelper.resizedBitmap(b, w, h);
        //        b.recycle();
        return b;
    }


    public static Bitmap createBitmap(int w, int h) {
        return createBitmap(w, h, Color.WHITE);
    }


    private static int getBaseStride(int w) {
        int stride = 2;
        while (stride < w) {
            stride *= 2;
        }
        return stride;
    }

    private static int[] createColors(@ColorInt int color, int length) {
        int[] colors = new int[length];
        for (int y = 0; y < colors.length; y++) {
            colors[y] = color;
        }
        return colors;
    }


    /**
     * Get Bitmap from filePath
     *
     * @param filePath in storage
     * @return bitmap
     */
    public static Bitmap getBitmap(String filePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = ImageConfig.BITMAP_CONFIG;
            return BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e) {
            Logger.E("GET BITMAP EXCEPTION: ", e);
            return null;
        }
    }


    /**
     * Save Bitmap at filePath
     *
     * @param bitmap        bitmap
     * @param filePath      path in storage
     * @param compressRatio [Nullable] default COMPRESS_RATIO
     * @param format        [Nullable] default Bitmap.CompressFormat.JPEG
     */
    public static boolean saveBitmap(Bitmap bitmap, String filePath, int compressRatio, Bitmap.CompressFormat format) {
        if (bitmap == null) return false;
        if (filePath == null || filePath.isEmpty()) return false;

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
            bitmap.compress(format, compressRatio, fileOutputStream);
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            Logger.E("SAVE BITMAP EXCEPTION: ", e);
            return false;
        }
    }

    public static void saveBitmap(Bitmap bitmap, String filePath, int compressRatio) {
        Bitmap.CompressFormat format = compressRatio == 100 ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;
        saveBitmap(bitmap, filePath, compressRatio, format);
    }

    public static void saveBitmap(Bitmap bitmap, String filePath) {
        saveBitmap(bitmap, filePath, ImageConfig.COMPRESS_RATIO);
    }

    /**
     * This will compress the bitmap with full quality, but containing transparent regions.
     */
    public static void saveTransparentBitmap(Bitmap bitmap, String filePath) {
        saveBitmap(bitmap, filePath, 0, Bitmap.CompressFormat.PNG);
    }

    //    public static void saveWithoutCompres(Bitmap bitmap, String filePath) {
    //        URL url = new URL(filePath);
    //        URLConnection conexion = url.openConnection();
    //        conexion.connect();
    //        int lenghtOfFile = conexion.getContentLength();
    //        Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
    //        InputStream input = new BufferedInputStream(url.openStream());
    //        OutputStream output = new FileOutputStream("/sdcard/caldophilus.jpg");
    //        byte data[] = new byte[1024];
    //        long total = 0;
    //        while ((count = input.read(data)) != -1) {
    //            total += count;
    //            output.write(data, 0, count);
    //        }
    //        output.flush();
    //        output.close();
    //        input.close();
    //    }

    public static Bitmap rotatedBitmap(Bitmap originalBitmap, int rotation) {
        if (rotation == 0) {
            return originalBitmap;
        }

        Matrix matrix = new Matrix();
        matrix.preRotate(rotation);

        Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }


    public static int sizeOf(Bitmap data) {
        return data.getByteCount();
    }


    /**
     * Reshape Bitmap Method
     **/
    public static Bitmap resizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static Bitmap resizeBitmapWidth(Bitmap sourceBitmap, int targetWidth) {
        if (sourceBitmap.isRecycled()) return null;
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();

        float aspectRatio = (float) targetWidth / (float) width;
        Matrix matrix = new Matrix();
        matrix.postScale(aspectRatio, aspectRatio);
        return Bitmap.createBitmap(sourceBitmap, 0, 0, width, height, matrix, true);

    }

    public static Bitmap resizeBitmapHeight(Bitmap sourceBitmap, int targetHeight) {
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();

        float aspectRatio = (float) targetHeight / (float) height;
        Matrix matrix = new Matrix();
        matrix.postScale(aspectRatio, aspectRatio);
        return Bitmap.createBitmap(sourceBitmap, 0, 0, width, height, matrix, true);
    }


    /* Nun Test Yet */
    public static Bitmap resizeToSquare(Bitmap sourceBitmap) {
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();

        int targetWidth;
        int targetHeight;
        int begin_X = 0;
        int begin_Y = 0;

        float aspectRatio = 1.0f;

        if ((float) width / (float) height >= aspectRatio) {
            targetHeight = height;
            targetWidth = (int) ((float) targetHeight * aspectRatio);
            begin_X = (width - targetWidth) / 2;
        } else {
            targetWidth = width;
            targetHeight = (int) ((float) targetWidth / aspectRatio);
            begin_Y = (height - targetHeight) / 2;
        }

        return Bitmap.createBitmap(sourceBitmap, begin_X, begin_Y, targetWidth, targetHeight);
    }


    public static Bitmap centerCropToSquare(Bitmap sourceBitmap) {
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();

        int targetSize = 0;
        int begin_X = 0;
        int begin_Y = 0;

        if (width > height) {
            targetSize = height;
            begin_X = (width - targetSize) / 2;
        } else {
            targetSize = width;
            begin_Y = (height - targetSize) / 2;
        }

        return Bitmap.createBitmap(sourceBitmap, begin_X, begin_Y, targetSize, targetSize);
    }


    /* Nun Test Yet */
    public static Bitmap circleBitmap(Bitmap bitmap, int roundPx) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), ImageConfig.BITMAP_CONFIG);
            Canvas canvas = new Canvas(output);

            bitmap = resizeToSquare(bitmap);

            int size = bitmap.getWidth();
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, size, size);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(size / 2f, size / 2f, (float) roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        } catch (Exception e) {
            Logger.E(TAG, e);
            return null;
        }
    }

    /* Nun Test Yet */
    public static Bitmap roundedCornerBitmap(Bitmap bitmap, int roundPx) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), ImageConfig.BITMAP_CONFIG);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, (float) roundPx, (float) roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        } catch (Exception e) {
            Logger.E(TAG, e);
            return null;
        }
    }

    // Convert transparentColor to be transparent in a Bitmap.
    public static Bitmap makeTransparent(Bitmap bit, @ColorInt int transparentColor) {
        int width = bit.getWidth();
        int height = bit.getHeight();
        Bitmap myBitmap = Bitmap.createBitmap(width, height, ImageConfig.BITMAP_CONFIG);
        int[] allpixels = new int[myBitmap.getHeight() * myBitmap.getWidth()];
        bit.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        myBitmap.setPixels(allpixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < myBitmap.getHeight() * myBitmap.getWidth(); i++) {
            if (allpixels[i] == transparentColor) {
                allpixels[i] = Color.alpha(Color.TRANSPARENT);
            }
        }

        myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        return myBitmap;
    }

}
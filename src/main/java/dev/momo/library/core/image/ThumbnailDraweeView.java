//package dev.momo.library.core.image;
//
//import android.content.Context;
//import android.graphics.drawable.Animatable;
//import android.net.Uri;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.drawee.controller.BaseControllerListener;
//import com.facebook.drawee.controller.ControllerListener;
//import com.facebook.drawee.generic.GenericDraweeHierarchy;
//import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
//import com.facebook.drawee.interfaces.DraweeController;
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.facebook.imagepipeline.image.ImageInfo;
//import com.facebook.imagepipeline.request.ImageRequest;
//
//import momo.android.makeanapp.R;
//
///**
// * For load Image of two type (Thumb/Origin)
// * <p/>
// * Thumb File : a thumb file of origin file, will named with "THUMBLE_" (@see Constants.java)
// * Should work with MultiFileUploader.java and Amazon S3
// */
//public class ThumbnailDraweeView extends SimpleDraweeView {
//
//    public enum LOADING_MODE {
//        ONLY_THUMB, // Load thumb file only.
//        NO_THUMB, // Load Origin file only.
//        THUMB_FIRST // (Default) Load thumb first, change to Origin when it's ready.
//    }
//
//    LOADING_MODE loadingMode = LOADING_MODE.THUMB_FIRST;
//
//    String defaultImageRes = "camera";
//
//    String loadingUrl;
//    String thumbUrl;
//
//    OnResizeListener resizeListener;
//
//
//    public ThumbnailDraweeView(Context context) {
//        super(context);
//        if (!isInEditMode()) {
//            init();
//        }
//    }
//
//    public ThumbnailDraweeView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        if (!isInEditMode()) {
//            init();
//        }
//    }
//
//    public ThumbnailDraweeView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        if (!isInEditMode()) {
//            init();
//        }
//    }
//
//
//    void init() {
//        GenericDraweeHierarchyBuilder builder =
//                new GenericDraweeHierarchyBuilder(getResources());
//        GenericDraweeHierarchy hierarchy = builder
//                .setFadeDuration(300)
//                .build();
//
//        hierarchy.setPlaceholderImage(R.drawable.default_image);
//        setHierarchy(hierarchy);
//    }
//
//    //    public MultiImageView defaultImageRes(int res) {
//    ////	    placeholderImage
//    //        defaultImageRes = res;
//    ////        setPlaceholderImage(res);
//    //        return this;
//    //    }
//
//    @Override
//    public void setImageURI(Uri uri) {
//        if (uri == null) {
//            return;
//        }
//
//        if (uri.toString().isEmpty()) {
//            return;
//        }
//
//        super.setImageURI(uri);
//    }
//
//    public ThumbnailDraweeView setOnResize(OnResizeListener listener) {
//        this.resizeListener = listener;
//        return this;
//    }
//
//    public void setLoadingOption(LOADING_MODE loadingMode) {
//        this.loadingMode = loadingMode;
//    }
//
//    public void setImageURI(Uri highResUri, Uri lowResUri) {
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setLowResImageRequest(ImageRequest.fromUri(lowResUri))
//                .setImageRequest(ImageRequest.fromUri(highResUri))
//                .setOldController(getController())
//                .setControllerListener(controllerListener)
//                .build();
//
//        setController(controller);
//    }
//
//
//    public void setImageResource(String fileName) {
//        String uri = "res://R.drawable." + fileName;
//        setImageURI(Uri.parse(uri));
//    }
//
//    public void setImageFilePath(String url) {
//
//        if (url == null) {
//            return;
//        }
//
//        if (url.equals("")) {
//            return;
//        }
//
//
//        /** load from local storage **/
//        if (url.startsWith("/storage") || url.startsWith("storage")
//                || url.startsWith("data") || url.startsWith("/data")) {
//            if (url.startsWith("/")) {
//                url = url.substring(1);
//            }
//            url = "file://" + url;
//            loadingUrl = url;
//        }
//
//
//        /** load from http **/
//        else {
//            loadingUrl = url;
//        }
//
//        //		Logger.I("set ImageFilePath " + loadingUrl);
//        Uri uri = Uri.parse(loadingUrl);
//
//        if (thumbUrl == null || thumbUrl.isEmpty()) {
//            setImageURI(uri);
//        } else {
//            // use LOADING_MODE only if using image from s3
//            switch (loadingMode) {
//                case ONLY_THUMB:
//                    setImageURI(Uri.parse(thumbUrl));
//                case NO_THUMB:
//                    setImageURI(uri);
//                case THUMB_FIRST:
//                    setImageURI(uri, Uri.parse(thumbUrl));
//            }
//
//        }
//    }
//
//
//    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
//        @Override
//        public void onFinalImageSet(
//                String id,
//                @Nullable ImageInfo imageInfo,
//                @Nullable Animatable anim) {
//            if (imageInfo == null) {
//                return;
//            }
//            if (resizeListener != null) {
//                resizeListener.onResize(imageInfo.getWidth(), imageInfo.getHeight());
//            }
//        }
//
//        @Override
//        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
//        }
//
//        @Override
//        public void onFailure(String id, Throwable throwable) {
//        }
//    };
//
//
//    public void setResizeListener(OnResizeListener resizeListener) {
//        this.resizeListener = resizeListener;
//    }
//}
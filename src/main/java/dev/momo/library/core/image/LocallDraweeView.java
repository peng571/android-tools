//package dev.momo.library.core.image;
//
//import android.content.Context;
//import android.graphics.drawable.Animatable;
//import android.net.Uri;
//import android.support.annotation.DrawableRes;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.drawee.controller.BaseControllerListener;
//import com.facebook.drawee.controller.ControllerListener;
//import com.facebook.drawee.drawable.ProgressBarDrawable;
//import com.facebook.drawee.drawable.ScalingUtils;
//import com.facebook.drawee.generic.GenericDraweeHierarchy;
//import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
//import com.facebook.drawee.interfaces.DraweeController;
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.facebook.imagepipeline.image.ImageInfo;
//import com.facebook.imagepipeline.request.ImageRequest;
//
//import dev.momo.library.core.Logger;
//import momo.android.makeanapp.R;
//
///**
// * For load local image in storage
// */
//public class LocallDraweeView extends SimpleDraweeView {
//
//    String loadingUrl;
//
//    @DrawableRes
//    int defaultImageRes;
//
//    public LocallDraweeView(Context context) {
//        super(context);
//        if (!isInEditMode()) {
//            init();
//        }
//    }
//
//    public LocallDraweeView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        if (!isInEditMode()) {
//            init();
//        }
//    }
//
//    public LocallDraweeView(Context context, AttributeSet attrs, int defStyle) {
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
//                .build();
//        setHierarchy(hierarchy);
//    }
//
//
//    //    public LocallDraweeView defaultImageRes(int res) {
//    //        defaultImageRes = res;
//    //        return this;
//    //    }
//    //
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
//        Logger.I("setImageURI %s", uri.toString());
//        super.setImageURI(uri);
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
//        Logger.I("setImageFilePath %s", url);
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
//        Logger.I("set ImageFilePath " + loadingUrl);
//        Uri uri = Uri.parse(loadingUrl);
//        setImageURI(uri);
//    }
//}
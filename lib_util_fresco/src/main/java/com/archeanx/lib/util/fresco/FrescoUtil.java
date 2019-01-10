package com.archeanx.lib.util.fresco;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by DEV on 2018/3/8.
 */

public class FrescoUtil {

    public static String getShowAssets(String imageName){
       return Uri.parse("asset:///ico-image/" + imageName).toString();
    }

    /**
     * 显示 assets的图片
     *
     * @param draweeView
     * @param imageName  图片名字 不带后缀
     */
    public static void showAssetsToPng(SimpleDraweeView draweeView, @NonNull String imageName) {
        if (!TextUtils.isEmpty(imageName)) {
            showAssets(draweeView, imageName + ".png");
        }
    }

    public static void showAssetsToJpg(SimpleDraweeView draweeView, @NonNull String imageName) {
        if (!TextUtils.isEmpty(imageName)) {
            showAssets(draweeView, imageName + ".jpg");
        }
    }

    /**
     * 显示 assets 中ico-image的图片
     *
     * @param draweeView
     * @param imageName  图片名字
     */
    public static void showAssets(SimpleDraweeView draweeView, String imageName) {
        if (!TextUtils.isEmpty(imageName)) {
            draweeView.setImageURI(Uri.parse("asset:///ico-image/" + imageName));
        }
    }

    /**
     * 显示 assets 中ico-image的图片
     *
     * @param draweeView
     * @param imageName  图片名字
     */
    public static void showWeatherToAssets(SimpleDraweeView draweeView, String imageName) {
        if (!TextUtils.isEmpty(imageName)) {
            draweeView.setImageURI(Uri.parse("asset:///cond_icon_heweather/" + imageName));
        }
    }

    /**
     * 显示 assets的图片
     *
     * @param draweeView
     * @param imageName  图片名字
     */
    public static void showAssets(String url, SimpleDraweeView draweeView, String imageName) {
        if (!TextUtils.isEmpty(imageName)) {
            draweeView.setImageURI(Uri.parse("asset:///ico-image/" + imageName));
        }
    }


    /**
     * 以高斯模糊显示。
     *
     * @param draweeView View。
     * @param url        url.
     * @param iterations 迭代次数，越大越魔化。
     * @param blurRadius 模糊图半径，必须大于0，越大越模糊。
     */
    public static void showUrlBlur(SimpleDraweeView draweeView, String url, int resId, int iterations, int blurRadius) {
        try {
            Uri uri = Uri.parse(url);
            ImageRequest request = ImageRequestBuilder.newBuilderWithResourceId(resId)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(iterations, blurRadius))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setImageRequest(request)
                    .build();
            draweeView.setController(controller);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.archeanx.lib.util.fresco;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by DEV on 2018/3/8.
 */

public class FrescoUtil {

    public static void init(Context context) {
        //对ImagePipelineConfig进行一些配置
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setDownsampleEnabled(true)// 对图片进行自动缩放
                .setResizeAndRotateEnabledForNetwork(true) // 对网络图片进行resize处理，减少内存消耗
                .setBitmapsConfig(Bitmap.Config.RGB_565)//图片设置RGB_565，减小内存开销  fresco默认情况下是RGB_8888
                .build();
        Fresco.initialize(context, config);
    }


    public static String getShowAssets(String imageName) {
        return Uri.parse("asset:///ico-image/" + imageName).toString();
    }

    public static Uri getShowAssetsUri(String imageName) {
        return Uri.parse("asset:///ico-image/" + imageName);
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


    /**
     * 显示缩略图
     *
     * @param draweeView     draweeView
     * @param url            url
     * @param resizeWidthPx  resizeWidth
     * @param resizeHeightPx resizeHeight
     */
    public static void showThumb(SimpleDraweeView draweeView, String url, int resizeWidthPx, int resizeHeightPx) {
        if (url == null || "".equals(url))
            return;
        if (draweeView == null)
            return;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setResizeOptions(new ResizeOptions(resizeWidthPx, resizeHeightPx))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        draweeView.setController(controller);
    }

    /**
     * 通过imageWidth 的宽度，自动适应高度
     * * @param simpleDraweeView view
     * * @param imagePath  Uri
     * * @param imageWidth width
     */
    public static void setControllerListener(final SimpleDraweeView simpleDraweeView, String imagePath, final int imageWidth) {
        final ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                int height = imageInfo.getHeight();
                int width = imageInfo.getWidth();
                layoutParams.width = imageWidth;
                layoutParams.height = (int) ((float) (imageWidth * height) / (float) width);
                simpleDraweeView.setLayoutParams(layoutParams);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                //Log.d("TAG", "Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                throwable.printStackTrace();
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder().setControllerListener(controllerListener).setUri(Uri.parse(imagePath)).build();
        simpleDraweeView.setController(controller);
    }

}

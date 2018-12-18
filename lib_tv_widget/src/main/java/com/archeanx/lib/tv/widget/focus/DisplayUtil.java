package com.archeanx.lib.tv.widget.focus;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by DEV on 2018/5/9.
 */

public class DisplayUtil {

    public static final float ZOOM_SCALE = 1.03f;
    private static final float ORIGIN_SCALE = 1.0f;
    public static void setViewAnimator(View v, boolean focus) {
        setViewAnimator(v, focus,300, ZOOM_SCALE);
    }


    public static void setViewAnimator(View v, boolean focus, int duration, float... params) {
        //组合动画
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX;
        ObjectAnimator scaleY;
        if (focus) {
            scaleX = ObjectAnimator.ofFloat(v, "scaleX", ORIGIN_SCALE, params[0]);
            scaleY = ObjectAnimator.ofFloat(v, "scaleY", ORIGIN_SCALE, params[0]);
        } else {
            scaleX = ObjectAnimator.ofFloat(v, "scaleX", params[0], ORIGIN_SCALE);
            scaleY = ObjectAnimator.ofFloat(v, "scaleY", params[0], ORIGIN_SCALE);
        }
        if (params.length > 1) {
            v.setPivotX(params[1]);
            v.setPivotY(params[2]);
        }
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSet.start();
    }
}

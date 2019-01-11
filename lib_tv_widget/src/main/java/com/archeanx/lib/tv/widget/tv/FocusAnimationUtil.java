package com.archeanx.lib.tv.widget.tv;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

public class FocusAnimationUtil {

    private static int mFocusWidth;
    private static int mFoucsHeight;


    /**
     * 焦点渐变显示动画
     */
    public static void focusAlphaAnimator(final View view, final View focusView) {
        focusAlphaAnimator(view, focusView, 1);
    }


    public static void focusAlphaAnimator(final View view, final View focusView, float scale) {
        focusAlphaAnimator(view,focusView,scale,43,43);
    }


    /**
     * 焦点渐变显示动画
     * 焦点view 渐变显示在 目标view上
     */
    public static void focusAlphaAnimator(final View view, final View focusView, float scale, int offSetX, int offSetY) {

        // 先判断，下一个即将获取焦点的目标veiw的坐标 是不是超过屏幕宽高
        int[] toLocation = new int[2];
        view.getLocationOnScreen(toLocation);


        float toWidth = view.getWidth() * scale + offSetX;
        float toHeight = view.getHeight() * scale + offSetY;
        float toX = toLocation[0] - (toWidth - view.getWidth()) / 2;
        float toY = toLocation[1] - (toHeight - view.getHeight()) / 2;

        focusView.setVisibility(View.GONE);

        focusView.setX(toX);
        focusView.setY(toY);

        ViewGroup.LayoutParams layoutParams = focusView.getLayoutParams();
        layoutParams.width = Float.valueOf(toWidth).intValue();
        layoutParams.height = Float.valueOf(toHeight).intValue();
        focusView.setLayoutParams(layoutParams);

        focusView.setVisibility(View.VISIBLE);
        //再动画显示
        ObjectAnimator animator = ObjectAnimator.ofFloat(focusView, "alpha", 0, 1);
        animator.setDuration(200);
        animator.start();

    }

    public static void focusMoveAnimator(View v, View onFousView) {
        focusMoveAnimator(v, onFousView, -1, 0, 0);
    }

    public static void focusMoveAnimatorBig(View v, View onFousView) {
        focusMoveAnimatorBig(v, onFousView, -1, 1.1f);
    }

    public static void focusMoveAnimatorBig(View v, View onFousView, float scale) {
        focusMoveAnimatorBig(v, onFousView, -1, scale);
    }

    /**
     * 焦点移动显示动画 放大比例控制
     *
     * @param view      即将获取到焦点的view
     * @param focusView 焦点view
     * @param scrollY
     */
    public static void focusMoveAnimatorBig(final View view, final View focusView, final int scrollY, float scale) {
        int[] toLocation = new int[2];
        view.getLocationOnScreen(toLocation);

        float toWidth = view.getWidth() * scale + 43;
        float toHeight = view.getHeight() * scale + 43;
        float toX = toLocation[0] - (toWidth - view.getWidth()) / 2;
        float toY = toLocation[1] - (toHeight - view.getHeight()) / 2;

        //焦点view
        int[] fromLocation = new int[2];
        focusView.getLocationOnScreen(fromLocation);

        float fromWidth = focusView.getWidth();
        float fromHeight = focusView.getHeight();
        float fromX = fromLocation[0];
        float fromY = fromLocation[1];

        if (scrollY == -1) {
            if (focusView.getVisibility() == View.GONE)
                focusView.setVisibility(View.VISIBLE);
        }

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(focusView, "x", fromX, toX);
        translateXAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                if (focusView.getVisibility() == View.GONE) {
//                    focusView.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
//                if (focusView.getVisibility() == View.GONE)
//                    focusView.setVisibility(View.VISIBLE);
            }
        });
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(focusView, "y", fromY, toY);
        ValueAnimator scaleWidthAnimator = ObjectAnimator.ofFloat(focusView, "width", fromWidth, toWidth);
        scaleWidthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float width = (Float) animation.getAnimatedValue();
                mFocusWidth = (int) width;
                ViewGroup.LayoutParams layoutParams = focusView.getLayoutParams();
                layoutParams.width = mFocusWidth;
                layoutParams.height = mFoucsHeight;
                focusView.setLayoutParams(layoutParams);
            }
        });
        ValueAnimator scaleHeightAnimator = ObjectAnimator.ofFloat(focusView, "height", fromHeight, toHeight);
        scaleHeightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float height = (Float) animation.getAnimatedValue();
                mFoucsHeight = (int) height;
                ViewGroup.LayoutParams layoutParams = focusView.getLayoutParams();
                layoutParams.width = mFocusWidth;
                layoutParams.height = mFoucsHeight;
                focusView.setLayoutParams(layoutParams);
            }
        });
        animatorSet.playTogether(translateXAnimator, translateYAnimator, scaleWidthAnimator, scaleHeightAnimator);
        animatorSet.setDuration(300);
        animatorSet.start();

    }


    /**
     * 焦点移动显示动画  宽高设置比例放大
     *
     * @param view      即将获取到焦点的view
     * @param focusView 焦点view
     * @param scrollY
     * @param offSetX
     * @param offSetY
     */
    public static void focusMoveAnimator(final View view, final View focusView, final int scrollY, int offSetX, int offSetY) {

        int[] toLocation = new int[2];
        view.getLocationOnScreen(toLocation);

        int toWidth = view.getWidth() + offSetX;
        int toHeight = view.getHeight() + offSetY;
        float toX = toLocation[0] - offSetX / 2;
        float toY = toLocation[1] - offSetY / 2;


        //焦点view
        int[] fromLocation = new int[2];
        focusView.getLocationOnScreen(fromLocation);

        float fromWidth = focusView.getWidth();
        float fromHeight = focusView.getHeight();
        float fromX = fromLocation[0];
        float fromY = fromLocation[1];


        if (scrollY == -1) {
            if (focusView.getVisibility() == View.GONE)
                focusView.setVisibility(View.VISIBLE);
        }

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(focusView, "x", fromX, toX);
        translateXAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                if (focusView.getVisibility() == View.GONE) {
//                    focusView.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
//                if (focusView.getVisibility() == View.GONE)
//                    focusView.setVisibility(View.VISIBLE);
            }
        });
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(focusView, "y", fromY, toY);
        ValueAnimator scaleWidthAnimator = ObjectAnimator.ofFloat(focusView, "width", fromWidth, toWidth);
        scaleWidthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float width = (Float) animation.getAnimatedValue();
                mFocusWidth = (int) width;
                ViewGroup.LayoutParams layoutParams = focusView.getLayoutParams();
                layoutParams.width = mFocusWidth;
                layoutParams.height = mFoucsHeight;
                focusView.setLayoutParams(layoutParams);
            }
        });
        ValueAnimator scaleHeightAnimator = ObjectAnimator.ofFloat(focusView, "height", fromHeight, toHeight);
        scaleHeightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float height = (Float) animation.getAnimatedValue();
                mFoucsHeight = (int) height;
                ViewGroup.LayoutParams layoutParams = focusView.getLayoutParams();
                layoutParams.width = mFocusWidth;
                layoutParams.height = mFoucsHeight;
                focusView.setLayoutParams(layoutParams);
            }
        });
        animatorSet.playTogether(translateXAnimator, translateYAnimator, scaleWidthAnimator, scaleHeightAnimator);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    //////////////--------放大效果--------/////////////

    public static final float ZOOM_SCALE = 1.03f;
    private static final float ORIGIN_SCALE = 1.0f;

    public static void setViewAnimatorBig(View v, boolean focus) {
        setViewAnimatorBig(v, focus,300, ZOOM_SCALE);
    }


    public static void setViewAnimatorBig(View v, boolean focus, int duration, float... params) {
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

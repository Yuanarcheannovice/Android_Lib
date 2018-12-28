package com.archeanx.lib.tv.widget.tv;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.archeanx.lib.tv.widget.R;

/**
 * @创建者
 * @创建时间
 * @描述 焦点移动工具类
 */
public class FocusMoveLauncherUtil {
    /**
     * 在activity中初始化
     */
    public static void initToActivity(Activity activity, @DrawableRes int focusImg) {
        //设置view的焦点被选中监听
        final View focusView = new View(activity);
        if (focusImg == 0) {
            focusImg = R.drawable.ico_focus_border_fillet;
        }
        focusView.setBackgroundResource(focusImg);
        ((ViewGroup) activity.getWindow().getDecorView()).addView(focusView);

        activity.getWindow().getDecorView().getViewTreeObserver().
                addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {

                    @Override
                    public void onGlobalFocusChanged(final View oldFocus, final View newFocus) {
                        if (oldFocus != null && FocusMoveLauncherHelper.getInstance().getBigAnimViews().get(oldFocus.getId()) == null) {
                            //去掉一般
                            FocusAnimationUtil.setViewAnimatorBig(oldFocus, false, 300, 1.1f);
                        }

                        if (newFocus == null) {
                            return;
                        }

                        if (FocusMoveLauncherHelper.getInstance().getNoAnimViews().get(newFocus.getId()) != null) {
                            focusView.setVisibility(View.GONE);
                            return;
                        }
                        if (FocusMoveLauncherHelper.getInstance().getBigAnimViews().get(newFocus.getId()) != null) {
                            //只能放大  焦点过去后，先放大view，然后焦点框渐变显示
                            if (focusView.getVisibility() == View.VISIBLE) {
                                focusView.setVisibility(View.GONE);
                            }
                            //显示在其他view 上面
                            newFocus.bringToFront();
                            FocusAnimationUtil.setViewAnimatorBig(newFocus, true, 300, 1.1f);
                            FocusAnimationUtil.focusAlphaAnimator(newFocus, focusView, 1.1f);
                        } else if (FocusMoveLauncherHelper.getInstance().getMoveAnimViews().get(newFocus.getId()) != null) {
                            //只能移动 没有放大效果，焦点框移动
                            if (focusView.getVisibility() == View.GONE) {
                                focusView.setVisibility(View.VISIBLE);
                            }
                            FocusAnimationUtil.focusMoveAnimator(newFocus, focusView, -1, 43, 43);
                        } else {
                            //放大和移动
                            if (focusView.getVisibility() == View.GONE) {
                                focusView.setVisibility(View.VISIBLE);
                            }
                            //显示在其他view 上面
                            newFocus.bringToFront();
                            FocusAnimationUtil.setViewAnimatorBig(newFocus, true, 300, 1.1f);
                            FocusAnimationUtil.focusMoveAnimatorBig(newFocus, focusView, 1.1f);
                        }
                    }

                });
    }


}

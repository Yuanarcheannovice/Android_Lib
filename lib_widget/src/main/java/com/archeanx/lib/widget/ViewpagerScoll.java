package com.archeanx.lib.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by DEV on 2018/6/2.
 */

public class ViewpagerScoll extends Scroller {

    private int mDuration = 1500; // 默认滑动速度 1500ms

    public ViewpagerScoll(Context context) {
        super(context);
    }

    public ViewpagerScoll(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    /**
     * set animation time
     *
     * @param time
     */
    public void setmDuration(int time) {
        mDuration = time;
    }

    /**
     * get current animation time
     *
     * @return
     */
    public int getmDuration() {
        return mDuration;
    }

    /**
     * 设置viewpager的滑动速度
     * @param DurationSwitch 毫秒
     */
    public static void controlViewPagerSpeed(Context context, ViewPager viewpager, int DurationSwitch) {
        try {

            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);

            ViewpagerScoll scroller = new ViewpagerScoll(context,
                    new AccelerateInterpolator());
            scroller.setmDuration(DurationSwitch);
            field.set(viewpager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
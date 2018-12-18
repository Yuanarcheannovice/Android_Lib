package com.archeanx.lib.tv.widget.tv;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.archeanx.lib.tv.widget.R;


/**
 * @创建者 xz
 * @创建时间 2018-11-09
 * @描述 基于viewpager 的 Launcher 焦点移动 帮助类
 * <p>
 * DisplayUtil ：放大缩小工具
 * FocusMoveAnimationUtil ： 焦点框移动工具
 * <p>
 * 此帮助类，实现了 两个动画（焦点框移动动画,焦点View放大缩小动画） 一个效果（阴影效果），
 */
public class FocusMoveViewpagerLauncherHelpler_old {

    private FocusMoveViewpagerLauncherHelpler_old() {

    }

    private static FocusMoveViewpagerLauncherHelpler_old instance;

    public static synchronized FocusMoveViewpagerLauncherHelpler_old getInstance() {
        if (instance == null) {
            instance = new FocusMoveViewpagerLauncherHelpler_old();
        }
        return instance;
    }

    private ViewTreeObserver.OnGlobalFocusChangeListener mOnGlobalFocusChangeListener;

    /**
     * 放到倍数
     */
    private float mScale = 1.1f;

    private Activity mActivity;

    /**
     * 左边fragment最大View的id
     */
    private int mLeftParentViewId = 0;

    /**
     * 右边fragment最大view的id
     */
    private int mRightParentViewId = 0;

    /**
     * 当viewpager翻页后，左边展示给用户看的焦点view Id
     */
    private int mLeftFocusViewId = 0;

    /**
     * 当viewpager翻页后，右边展示给用户看的焦点view Id
     */
    private int mRightFocusViewId = 0;

    /**
     * 是否是Rv布局
     */
    private boolean isRecyclerView = false;

    /**
     * 是否是可滑动布局
     */
    private boolean isScollView = false;


    /**
     * 用来存放 只需要移动动画的view
     */
    private ArrayMap<Integer, Integer> mMoveAnimViews = new ArrayMap<>();

    /**
     * 用来存放，给了焦点，又不需要动画效果的view
     */
    private ArrayMap<Integer, Integer> mNoAnimViews = new ArrayMap<>();

    /**
     * 添加 只需要 移动动画的 view
     * （默认带有放大和移动动画，如果只需要移动动画，那么需要把 目标view 添加到mMoveAnimViews 集合中）
     */
    public void addMoveAnimationView(@IdRes Integer viewId) {
        mMoveAnimViews.put(viewId, viewId);
    }

    /**
     * 添加 给了焦点 又不需要动画的view
     */
    public void addNoAnimationView(@IdRes Integer viewId) {
        mNoAnimViews.put(viewId, viewId);
    }


    public void setRecyclerView(boolean recyclerView) {
        isRecyclerView = recyclerView;
    }

    public void setScollView(boolean scollView) {
        isScollView = scollView;
    }

    /**
     * 初始化包裹fragment的activity
     */
    public void initToMain(Activity activity) {
        mActivity = activity;
        //设置view的焦点被选中监听
        final View focusView = new View(activity);
        focusView.setBackgroundResource(R.drawable.ico_focus_border_right_angle);
        ((ViewGroup) activity.getWindow().getDecorView()).addView(focusView);

        activity.getWindow().getDecorView().getViewTreeObserver().
                addOnGlobalFocusChangeListener(mOnGlobalFocusChangeListener = new ViewTreeObserver.OnGlobalFocusChangeListener() {

                    @Override
                    public void onGlobalFocusChanged(final View oldFocus, final View newFocus) {
                        FocusAnimationUtil.setViewAnimatorBig(oldFocus, false);

                        //显示在其他view 上面
                        newFocus.bringToFront();
                        FocusAnimationUtil.setViewAnimatorBig(newFocus, true, 300, mScale);
                        FocusAnimationUtil.focusMoveAnimatorBig(newFocus, focusView, mScale);
                    }

                });
    }

    /**
     * 初始化左边的fragment
     * 在Viewpager翻页时，让fragment的最大布局优先获取到焦点，再手动传递给指定view焦点，防止viewpager翻页导致的焦点混乱
     *
     * @param parentView      fragment最大布局的View
     * @param pagingFocusView viewpager翻页后需要显示给用户看得焦点view
     */
    public void initToLeftFragment(@NonNull final ViewGroup parentView, final View pagingFocusView) {
        mLeftParentViewId = parentView.getId();
        mLeftFocusViewId = pagingFocusView.getId();
        //会优先其子类控件而获取到焦点
        parentView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        //设置焦点
        parentView.setFocusable(true);
        parentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //禁用父布局所有键位事件，然后把焦点传递给 目标View，手动控制焦点移动
                return true;
            }
        });
        parentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    parentView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pagingFocusView.requestFocus();
                        }
                    }, 300);
                }
            }
        });
    }


    /**
     * 初始化右边的fragment
     * 在Viewpager翻页时，让fragment的最大布局优先获取到焦点，再手动传递给指定view焦点，防止viewpager翻页导致的焦点混乱
     *
     * @param parentView      fragment最大布局的View
     * @param pagingFocusView viewpager翻页后需要显示给用户看得焦点view
     */
    public void initToRgihtFragment(@NonNull final ViewGroup parentView, final View pagingFocusView) {
        mRightParentViewId = parentView.getId();
        mRightFocusViewId = pagingFocusView.getId();
        //会优先其子类控件而获取到焦点
        parentView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        //设置焦点
        parentView.setFocusable(true);
        parentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //禁用父布局所有键位事件，然后把焦点传递给 目标View，手动控制焦点移动
                return true;
            }
        });
        parentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    parentView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pagingFocusView.requestFocus();
                        }
                    }, 300);
                }
            }
        });
    }


    /**
     * 释放，记得释放
     */
    public void release() {
        if (mActivity != null && mOnGlobalFocusChangeListener != null) {
            mActivity.getWindow().getDecorView().getViewTreeObserver()
                    .removeOnGlobalFocusChangeListener(mOnGlobalFocusChangeListener);
        }
        mActivity = null;
        mOnGlobalFocusChangeListener = null;
        mMoveAnimViews.clear();
        mMoveAnimViews = null;
        instance = null;
    }

}

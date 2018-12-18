package com.archeanx.lib.tv.widget.focus;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.archeanx.lib.tv.widget.R;


/**
 * Created by xz on 2018/5/9.
 * 焦点放大
 */

public class FocusLauncherRelativeLayout extends RelativeLayout implements View.OnFocusChangeListener, View.OnHoverListener {

    private float mScale;
    private int mDuration;
    public FocusLauncherRelativeLayout(Context context) {
        super(context);
    }

    public FocusLauncherRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public FocusLauncherRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FocusLauncherRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TvFocusLayout, 0, 0);
        mScale = a.getFloat(R.styleable.TvFocusLayout_layout_scale, 1.01f);
        mDuration = a.getInt(R.styleable.TvFocusLayout_layout_duration, 300);
        a.recycle();
        this.setOnFocusChangeListener(this);
        this.setOnHoverListener(this);
    }

    @Override
    public void onFocusChange(final View v, final boolean hasFocus) {
        //放大缩小动画
        if (hasFocus) {
            //显示在其他view 上面
            v.bringToFront();
        }
        DisplayUtil.setViewAnimator(v, hasFocus, mDuration, mScale);
    }

    /**
     * 鼠标离开进入
     */
    @Override
    public boolean onHover(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
            DisplayUtil.setViewAnimator(v, true, mDuration, mScale);
        } else if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
            DisplayUtil.setViewAnimator(v, false, mDuration, mScale);
        }
        return false;
    }

}

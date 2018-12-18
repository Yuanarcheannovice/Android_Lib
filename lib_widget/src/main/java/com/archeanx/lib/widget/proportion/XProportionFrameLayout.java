package com.archeanx.lib.widget.proportion;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.archeanx.lib.widget.R;

/**
 * Created by xz on 2017/9/12 0012.
 */

public class XProportionFrameLayout extends FrameLayout {
    /**
     * 宽比例
     */
    private final float mWidthRatio;

    /**
     * 高比例
     */
    private final float mHeightRatio;


    /**
     * 是否以宽为基准计算
     */
    private final boolean isWidthTarget;


    public XProportionFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.XProportionLayout);
        mWidthRatio = typedArray.getFloat(R.styleable.XProportionLayout_xplWidthRatio, -1);
        mHeightRatio = typedArray.getFloat(R.styleable.XProportionLayout_xplHeightRatio, -1);
        isWidthTarget = typedArray.getBoolean(R.styleable.XProportionLayout_xplIsWidthTarget, true);
        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mWidthRatio != -1 && mHeightRatio != -1) {
            if (isWidthTarget) {
                if (mWidthRatio == 1 && mHeightRatio == 1) {
                    heightMeasureSpec = widthMeasureSpec;
                } else {
                    final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
                    final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.round(mHeightRatio / mWidthRatio * widthSize), widthMode);
                }
            } else {
                if (mWidthRatio == 1 && mHeightRatio == 1) {
                    widthMeasureSpec = heightMeasureSpec;
                } else {
                    final int heightMode = MeasureSpec.getMode(widthMeasureSpec);
                    final int heightSize = MeasureSpec.getSize(widthMeasureSpec);
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.round(mWidthRatio / mHeightRatio * heightSize), heightMode);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}

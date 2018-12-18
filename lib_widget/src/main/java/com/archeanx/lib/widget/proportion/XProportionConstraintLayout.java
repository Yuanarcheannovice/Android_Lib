package com.archeanx.lib.widget.proportion;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;

import com.archeanx.lib.widget.R;


/**
 * Created by xz on 2017/9/8 0008.
 * 比例RelativeLayout
 * 根据宽来设置高
 */

public class XProportionConstraintLayout extends ConstraintLayout {
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

    public XProportionConstraintLayout(Context context, AttributeSet attrs) {
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
                    final int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
                    final int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
                    heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.round(mHeightRatio / mWidthRatio * widthSize), widthMode);
                }
            } else {
                if (mWidthRatio == 1 && mHeightRatio == 1) {
                    widthMeasureSpec = heightMeasureSpec;
                } else {
                    final int heightMode = View.MeasureSpec.getMode(widthMeasureSpec);
                    final int heightSize = View.MeasureSpec.getSize(widthMeasureSpec);
                    widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.round(mWidthRatio / mHeightRatio * heightSize), heightMode);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}

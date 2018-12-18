package com.archeanx.lib.widget.divider;


import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.archeanx.lib.widget.R;
import com.archeanx.lib.widget.divider.util.Y_Divider;
import com.archeanx.lib.widget.divider.util.Y_DividerBuilder;
import com.archeanx.lib.widget.divider.util.Y_DividerItemDecoration;


/**
 * @author xcoder_xz
 * @date 8017/5/1 0001
 */

public class XRvVerDivider extends Y_DividerItemDecoration {
    private int mColor;
    private int mWidthDp;
    private int mStartPadding;
    private int mEndPadding;
    private boolean isShowTopLine;
    private Y_Divider divider1;
    private Y_Divider divider2;

    private XRvVerDivider(Context context, int color, int widthDp, int startPadding, int endPadding, boolean isShowTopLine) {
        super(context);
        this.mColor = ContextCompat.getColor(context, color);
        this.mWidthDp = widthDp;
        this.mStartPadding = startPadding;
        this.mEndPadding = endPadding;
        this.isShowTopLine = isShowTopLine;
    }

    @Override
    public Y_Divider getDivider(int itemPosition) {
        if (itemPosition == 0 && isShowTopLine) {
            if (divider1 == null) {
                divider1 = new Y_DividerBuilder()
                        .setTopSideLine(true, mColor, mWidthDp, mStartPadding, mEndPadding)
                        .setBottomSideLine(true, mColor, mWidthDp, mStartPadding, mEndPadding)
                        .create();
            }
            return divider1;
        } else {
            if (divider2 == null) {
                divider2 = new Y_DividerBuilder()
                        .setBottomSideLine(true, mColor, mWidthDp, mStartPadding, mEndPadding)
                        .create();
            }
            return divider2;
        }
    }


    public static class Builder {
        private int mColor;
        private int mWidthDp = 1;
        private int mStartPadding = 0;
        private int mEndPadding = 0;
        private boolean isShowTopLine = false;
        private Context mContext;

        public Builder(Context context) {
            this.mContext = context;
            mColor = R.color.app_divider_color;
        }

        /**
         * 设置分割线颜色
         */
        public Builder setLineColorResoure(@ColorRes int color) {
            this.mColor = color;
            return this;
        }

        /**
         * 设置线条宽度/高度
         */
        public Builder setLineWidthDp(int widthDp) {
            this.mWidthDp = widthDp;
            return this;
        }

        /**
         * 设置开始边距
         */
        public Builder setStartPadding(int startPadding) {
            this.mStartPadding = startPadding;
            return this;
        }

        /**
         * 设置结束边距
         */
        public Builder setEndPadding(int endPadding) {
            this.mEndPadding = endPadding;
            return this;
        }

        public Builder setPadding(int startPadding, int endPadding) {
            this.mStartPadding = startPadding;
            this.mEndPadding = endPadding;
            return this;
        }

        /**
         * 是否显示第一行的分割线
         */
        public Builder setShowTopLine(boolean showTopLine) {
            this.isShowTopLine = showTopLine;
            return this;
        }

        public XRvVerDivider build() {
            return new XRvVerDivider(mContext, mColor, mWidthDp, mStartPadding, mEndPadding, isShowTopLine);
        }

    }
}
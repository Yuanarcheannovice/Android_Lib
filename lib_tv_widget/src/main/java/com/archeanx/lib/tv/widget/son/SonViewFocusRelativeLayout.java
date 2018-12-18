package com.archeanx.lib.tv.widget.son;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by DEV on 2018/5/8.
 */

public class SonViewFocusRelativeLayout extends RelativeLayout {


    public SonViewFocusRelativeLayout(Context context) {
        super(context);
    }

    public SonViewFocusRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SonViewFocusRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SonViewFocusRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            childAt.setSelected(gainFocus);
        }
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

    }

}

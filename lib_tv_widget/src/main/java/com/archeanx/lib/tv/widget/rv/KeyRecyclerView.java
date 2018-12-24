package com.archeanx.lib.tv.widget.rv;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by DEV on 2018/6/4.
 */

public class KeyRecyclerView extends RecyclerView {
    public KeyRecyclerView(Context context) {
        super(context);
    }

    public KeyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (dispatchKeyEventListener != null) {
            if (dispatchKeyEventListener.dispatchKeyEvent(event, event.getAction(), event.getKeyCode())) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


//////
    private DispatchKeyEventListener dispatchKeyEventListener;

    public void setDispatchKeyEventListener(DispatchKeyEventListener dispatchKeyEventListener) {
        this.dispatchKeyEventListener = dispatchKeyEventListener;
    }

    public interface DispatchKeyEventListener {
        boolean dispatchKeyEvent(KeyEvent event, int keyAction, int keyCode);
    }
}

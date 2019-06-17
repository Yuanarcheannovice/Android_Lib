package com.archeanx.lib.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;


/**
 * @author xz
 * @date 17/11/18
 */

public abstract class XBaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    protected GestureDetector gestureDetector;

    /**
     * 是否关闭
     */
    private boolean isClose = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onActivityInit();
        super.onCreate(savedInstanceState);
        //Activity管理
        XAppActivityManager.getInstance().addActivity(this);
        setContentView(onSetContentView(savedInstanceState));
        onInitView();
        onSetListener();
        onInitData();
    }

    protected void onActivityInit() {

    }

    /**
     * param savedInstanceState
     * 方法说明:初始化界 * 方法名称:onPreOnCreate
     * 返回void
     */
    @LayoutRes
    protected abstract int onSetContentView(Bundle savedInstanceState);

    /**
     * 初始化View
     */
    protected abstract void onInitView();

    /**
     * 设置监听
     */
    protected abstract void onSetListener();

    /**
     * 开始数据
     */
    protected abstract void onInitData();

    /**
     * 方法说明:手动释放内存
     * 方法名称:releaseMemory
     * 返回void
     */
    protected abstract void onCloseActivity();

    @Override
    protected void onPause() {
        if (isFinishing() && !isClose) {
            isClose = true;
            onCloseActivity();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (isFinishing() && !isClose) {
            isClose = true;
            onCloseActivity();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        XAppActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 右滑关闭Activity
     */
    public void onSlidingListener() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 != null && e2 != null) {
                    if ((e2.getX() - e1.getX()) > 0) {
                        finish();
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }
}

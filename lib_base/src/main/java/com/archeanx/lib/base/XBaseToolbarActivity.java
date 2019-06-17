package com.archeanx.lib.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

/**
 * @author xz
 * 带有toolbar 的activity
 */
public abstract class XBaseToolbarActivity extends XBaseActivity implements View.OnClickListener {

    /**
     * @param title 设置 标题
     */
    protected void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onInitView() {
        Toolbar toolbar = findViewById(showToolbar());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        onInitViewId();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param savedInstanceState savedInstanceState
     * @return 设置layout
     */
    @Override
    @LayoutRes
    protected abstract int onSetContentView(Bundle savedInstanceState);

    /**
     * 必须 指定toolbar id
     *
     * @return toolbar id
     */
    @IdRes

    protected abstract int showToolbar();

    /**
     * 初始化view
     */
    protected abstract void onInitViewId();


    /**
     * 设置监听
     */
    @Override
    protected abstract void onSetListener();


    /**
     * 点击事件
     *
     * @param v 被点击的view
     */
    @Override
    public abstract void onClick(View v);

    /**
     * 开始数据
     */
    @Override
    protected abstract void onInitData();

    /**
     * 方法说明:手动释放内存
     * 方法名称:releaseMemory
     * 返回void
     */
    @Override
    protected abstract void onCloseActivity();


}

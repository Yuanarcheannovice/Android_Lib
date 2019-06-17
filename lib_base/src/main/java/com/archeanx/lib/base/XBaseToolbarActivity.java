package com.archeanx.lib.base;

import android.support.annotation.IdRes;
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

    /**
     * 初始化view
     */
    protected abstract void onInitViewId();

    /**
     * 必须 指定toolbar id
     *
     * @return toolbar id
     */
    @IdRes

    protected abstract int showToolbar();


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

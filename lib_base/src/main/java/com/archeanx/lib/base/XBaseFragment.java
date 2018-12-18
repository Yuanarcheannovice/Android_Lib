package com.archeanx.lib.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by xz on 2017/6/30 0030.
 * <p>
 * 一般使用的fragment，配合FragmentTransition使用
 * <p>
 * fragment的显示隐藏，如果触发了onResume，则不会触发onHiddenChanged的显示;
 */

public abstract class XBaseFragment extends Fragment {
    /**
     * fragment的视图是否加载完毕
     */
    protected boolean isCreateView = false;

    private View mContentView;

    protected final String TAG = this.getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContentView = inflater.inflate(getContentView(), container, false);
        initView(mContentView);
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //当布局加载完成，并且界面显示的时候才会调用
        isCreateView = true;
        // 数据处理
        xLoadData();
    }

    @Override
    public void onDestroyView() {
        isCreateView = false;
        closeFragment();
        mContentView = null;
        super.onDestroyView();
    }

    @LayoutRes
    protected abstract int getContentView();

    protected abstract void initView(View view);//初始话view

    protected abstract void xLoadData();//可以加载数据了

    /**
     * [绑定控件]
     */
    protected <T extends View> T find(@IdRes int resId) {
        return (T) mContentView.findViewById(resId);
    }


    /**
     * 方法说明:手动释放内存
     */
    protected abstract void closeFragment();

}

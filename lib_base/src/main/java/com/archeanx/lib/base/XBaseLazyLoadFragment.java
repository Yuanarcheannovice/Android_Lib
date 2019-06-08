package com.archeanx.lib.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author xz
 * ViewPager 嵌套 Fragment时 使用
 */
public abstract class XBaseLazyLoadFragment extends Fragment {
    /**
     * fragment的视图是否加载完毕
     */
    protected boolean isCreateView = false;

    /**
     * Fragment是否已经展示
     */
    protected boolean isShowFragment = false;

    /**
     * 数据加载方式是否被调用
     */
    protected boolean isLoadData = false;

    protected View mContentView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null != mContentView) {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (null != parent) {
                parent.removeView(mContentView);
            }
        } else {
            mContentView = inflater.inflate(getContentView(), container, false);
            initView(mContentView);
        }
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isCreateView && isLoadData) {
            dispatchSetUserVisibleHint(isVisibleToUser);
        }
        this.isShowFragment = isVisibleToUser;
        xLoadData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (null != mContentView) {
            ((ViewGroup) mContentView.getParent()).removeView(mContentView);
        }
    }


    @Override
    public void onDestroy() {
        isLoadData = false;
        isShowFragment = false;
        isCreateView = false;
        closeFragment();
        mContentView = null;
        super.onDestroy();
    }

    /**
     * 判读 页面是否创建完，fragment是否显示在用户眼前，是否加载过数据（只允许加载一次数据）
     */
    private void xLoadData() {
        if (isCreateView && isShowFragment && !isLoadData) {
            lazyLoadData();
            isLoadData = true;
        }
    }

    @LayoutRes
    protected abstract int getContentView();

    /**
     * 初始view
     */
    protected abstract void initView(View view);

    /**
     * 加载数据
     */
    protected abstract void lazyLoadData();


    /**
     * 绑定控件
     *
     * @return view的实例
     */
    protected <T extends View> T find(@IdRes int resId) {
        return (T) mContentView.findViewById(resId);
    }

    /**
     * 方法说明:手动释放内存
     */
    protected abstract void closeFragment();


    /**
     * ViewPager场景下，判断父fragment是否可见
     *
     * @return
     */
    protected boolean isParentVisible() {
        Fragment fragment = getParentFragment();
        return fragment == null || (fragment instanceof XBaseLazyLoadFragment && ((XBaseLazyLoadFragment) fragment).isShowFragment);
    }

    /**
     * ViewPager场景下，当前fragment可见，如果其子fragment也可见，则尝试让子fragment请求数据
     */
    protected void dispatchParentVisibleState() {
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.isEmpty()) {
            return;
        }
        for (Fragment child : fragments) {
            if (child instanceof XBaseLazyLoadFragment && ((XBaseLazyLoadFragment) child).isShowFragment) {
                ((XBaseLazyLoadFragment) child).xLoadData();
            }
        }
    }

    private Fragment childFragment;

    /**
     * ViewPager场景下，当前fragment可见，如果其子fragment也可见，则尝试让子fragmentSetUserVisibleHint
     */
    protected void dispatchSetUserVisibleHint(boolean isVisibleToUser) {
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.isEmpty()) {
            return;
        }
        for (Fragment child : fragments) {
            if (child instanceof XBaseLazyLoadFragment) {
                if (!isVisibleToUser && child.getUserVisibleHint()) {
                    childFragment = child;
                } else if (isVisibleToUser && childFragment != null && !childFragment.getUserVisibleHint()) {
                    childFragment.setUserVisibleHint(true);
                    childFragment = null;
                }
            }
        }
    }
}

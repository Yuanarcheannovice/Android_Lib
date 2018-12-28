package com.archeanx.lib.tv.widget.tv;

import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.view.View;

/**
 * @创建者
 * @创建时间
 * @描述
 */
public class FocusMoveHelper {
    private FocusMoveHelper() {

    }

    private static FocusMoveHelper instance;


    public static synchronized FocusMoveHelper getInstance() {
        if (instance == null) {
            instance = new FocusMoveHelper();
        }
        return instance;
    }

    /**
     * 不需要动画效果的view
     */
    private ArrayMap<Integer, Integer> mNoAnimViews = new ArrayMap<>();

    /**
     * 只有放大效果的view
     */
    private ArrayMap<Integer, Integer> mBigAnimViews = new ArrayMap<>();

    /**
     * 只移动效果的view
     */
    private ArrayMap<Integer, Integer> mMoveAnimViews = new ArrayMap<>();

    /**
     * 绑定View的跳转
     */
    private ArrayMap<Integer, Integer> mAppointNextView = new ArrayMap<>();

    /**
     * 旧的焦点view id
     */
    private int mOldFocusViewId=View.NO_ID;

    /**
     * 新的焦点view id
     */
    private int mNewFocusViewId=View.NO_ID;

    /**
     * Tab栏 父布局view id
     *
     */
    private int mTabLayoutViewId=View.NO_ID;

    /**
     * viewpager的页码
     */
    private int mViewpageIndex=0;


    public int getViewpageIndex() {
        return mViewpageIndex;
    }

    public void setViewpageIndex(int viewpageIndex) {
        mViewpageIndex = viewpageIndex;
    }









    public int getOldFocusViewId() {
        return mOldFocusViewId;
    }

    public void setOldFocusViewId(int oldFocusViewId) {
        mOldFocusViewId = oldFocusViewId;
    }

    public int getNewFocusViewId() {
        return mNewFocusViewId;
    }

    public void setNewFocusViewId(int newFocusViewId) {
        mNewFocusViewId = newFocusViewId;
    }

    public int getTabLayoutViewId() {
        return mTabLayoutViewId;
    }

    /**
     * 设置 tab 栏 viewGroup id
     *
     */
    public void setTabLayoutViewId(int tabLayoutViewId) {
        mTabLayoutViewId = tabLayoutViewId;
    }

    public ArrayMap<Integer, Integer> getAppointNextView() {
        return mAppointNextView;
    }


    /**
     *
     * @return 根据旧的view 获取下一个需要获取焦点的view
     */
    public Integer getCorrespondingView() {

        if (mAppointNextView.get(mOldFocusViewId) == null) {
            return View.NO_ID;
        }
        return mAppointNextView.get(mOldFocusViewId);
    }

    /**
     * 绑定vie的跳转
     *
     * @param oldViewId 旧的view id
     * @param newViewId 新的view id
     */
    public void addAppointNextView(@NonNull Integer oldViewId, @NonNull Integer newViewId) {
        mAppointNextView.put(oldViewId, newViewId);
    }

    public ArrayMap<Integer, Integer> getNoAnimViews() {
        return mNoAnimViews;
    }

    public void addNoAnimViews(@NonNull Integer key, @NonNull Integer value) {
        mNoAnimViews.put(key, value);
    }


    public ArrayMap<Integer, Integer> getBigAnimViews() {
        return mBigAnimViews;
    }

    public void addBigAnimViews(@NonNull Integer key, @NonNull Integer value) {
        mBigAnimViews.put(key, value);
    }

    public ArrayMap<Integer, Integer> getMoveAnimViews() {
        return mMoveAnimViews;
    }

    public void addMoveAnimViews(@NonNull Integer key, @NonNull Integer value) {
        mMoveAnimViews.put(key, value);
    }
}

package com.archeanx.lib.tv.widget.tv;

import android.support.annotation.NonNull;
import android.util.ArrayMap;

/**
 * @创建者
 * @创建时间
 * @描述
 */
public class FocusMoveLauncherHelper {
    private FocusMoveLauncherHelper() {

    }

    private static FocusMoveLauncherHelper instance;


    public static synchronized FocusMoveLauncherHelper getInstance() {
        if (instance == null) {
            instance = new FocusMoveLauncherHelper();
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

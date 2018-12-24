package com.archeanx.lib.tv.widget.rv;

import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author xz
 * @date 2018/6/26
 * RecyclerView 在Tv端 焦点的辅助类
 * 保持焦点在 移出的位置，移入时还在原来的位置
 */

public class RvFocusedHelper {
    private static RvFocusedHelper instance;

    public static synchronized RvFocusedHelper getInstance() {
        if (instance == null) {
            instance = new RvFocusedHelper();
        }
        return instance;
    }

    private OnFocusRestoreListener mListener;

    private RvFocusedHelper() {

    }

    public void release() {
        mListener = null;
        instance = null;
    }


    /**
     * 当右边Fragment中的Rv，的焦点准备移入左边的Rv中时，需要拦截
     */
    public boolean onLeftRvFocusRestore() {
        if (mListener != null) {
            return mListener.onFocusRestore();
        } else {
            return false;
        }

    }

    /**
     * 当右边的Rv的焦点准备移到左边时的监听
     */
    public void setFocusRestoreListener(OnFocusRestoreListener listener) {
        this.mListener = listener;
    }

    /**
     * 初始化右边 竖直rv
     * 左移焦点时，需要告诉左边rv
     * 最后一个view应该向 下一个或者上一个按钮移动
     *
     * @param focuseDownView 右边rv向下移动焦点的view
     */
    public static void initRightHorizontalRvKey(final KeyRecyclerView recyclerView, final View focuseDownView, final int horizontalSize) {
        recyclerView.setDispatchKeyEventListener(new KeyRecyclerView.DispatchKeyEventListener() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent event, int keyAction, int keyCode) {
                if (keyAction == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        int childAdapterPosition = recyclerView.getChildAdapterPosition(recyclerView.getFocusedChild());
                        if (childAdapterPosition % horizontalSize == 0) {
                            //如果焦点需要向左移动时，需要回调
                            return RvFocusedHelper.getInstance().onLeftRvFocusRestore();
                        }
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        //右边rv最后一个再向下移动焦点时，需要移动到特定的view
                        try {
                            int childAdapterPosition = recyclerView.getChildAdapterPosition(recyclerView.getFocusedChild());
                            View childAt = recyclerView.getChildAt(childAdapterPosition + horizontalSize);
                            if (childAt == null && focuseDownView != null) {
                                focuseDownView.requestFocus();
                                return true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        int childAdapterPosition = recyclerView.getChildAdapterPosition(recyclerView.getFocusedChild());
                        View childAt = recyclerView.getChildAt(childAdapterPosition - horizontalSize);
                        if (childAt == null) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    /**
     * 初始化右边 竖直rv
     * 左移焦点时，需要告诉左边rv
     * 最后一个view应该向 下一个或者上一个按钮移动
     *
     * @param focuseView 右边rv向下移动焦点的view
     */
    public static void initRightVerticalRvKey(final KeyRecyclerView recyclerView, final View focuseView, @NonNull final OnInitRightRvListener listener) {
        recyclerView.setDispatchKeyEventListener(new KeyRecyclerView.DispatchKeyEventListener() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent event, int keyAction, int keyCode) {
                if (keyAction == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        //如果焦点需要向左移动时，需要回调
                        return RvFocusedHelper.getInstance().onLeftRvFocusRestore();
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        //右边rv最后一个再向下移动焦点时，需要移动到特定的view
                        try {
                            int childAdapterPosition = recyclerView.getChildAdapterPosition(recyclerView.getFocusedChild());
                            if (childAdapterPosition == listener.getRvDataSize() - 1) {
                                focuseView.requestFocus();
                                return true;
                            }
//                            View childAt = recyclerView.getChildAt(childAdapterPosition + 1);
//                            if (childAt == null && focuseView != null) {
//
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });
    }


    /**
     * 设置左边Rv的处理
     * 最后一个时不能向下，如果右边没有数据，不能往右边走
     */
    public static void initLeftRvKey(final KeyRecyclerView recyclerView, @NonNull final OnInitLeftRvListener listener) {
        recyclerView.setDispatchKeyEventListener(new KeyRecyclerView.DispatchKeyEventListener() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent event, int keyAction, int keyCode) {
                if (keyAction == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        //如果左边leftRv最后一个有焦点，则不允许有向下的焦点
                        int childAdapterPosition = recyclerView.getChildAdapterPosition(recyclerView.getFocusedChild());
                        if (childAdapterPosition == listener.getRvDataSize() - 1) {
                            return true;
                        }
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        //如果右边没有数据，则不允许往右边转移焦点
                        int childAdapterPosition = recyclerView.getChildAdapterPosition(recyclerView.getFocusedChild());
                        if (!listener.isHaveDataToRight(childAdapterPosition)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }


    /**
     * 限制上一页只能向rv 转移焦点
     */
    public static void keyToTopView(View view, final KeyRecyclerView rv) {
        //限制上一页只能向rv 转移焦点
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (rv.getChildCount() > 0) {
                            rv.getChildAt(rv.getChildCount() - 1).requestFocus();
                        }
                    }
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    //去掉左移焦点
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 限制下一页只能向rv 转移焦点
     */
    public static void keyToNextView(View view, final KeyRecyclerView rv) {
        //限制下一页只能向上面的rv 转移焦点
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (rv.getChildCount() > 0) {
                            rv.getChildAt(rv.getChildCount() - 1).requestFocus();
                        }
                    }
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    //去掉右移焦点
                    return true;
                }
                return false;
            }
        });
    }


    /**
     * 当这个
     */
    public interface OnFocusRestoreListener {
        /**
         * 焦点复原
         */
        boolean onFocusRestore();
    }

    public interface OnInitLeftRvListener {

        /**
         * 获取左边rv的数据量
         */
        int getRvDataSize();

        /**
         * 右边是否有数据
         *
         * @param position 左边的item的下标
         */
        boolean isHaveDataToRight(int position);
    }

    public interface OnInitRightRvListener {

        /**
         * 获取左边rv的数据量
         */
        int getRvDataSize();

    }
}

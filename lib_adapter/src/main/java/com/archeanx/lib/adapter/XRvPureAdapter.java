package com.archeanx.lib.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.archeanx.lib.adapter.xutil.XRvViewHolder;


/**
 * 一个简单封装了，Rl点击事件的类，
 *
 * @author xz
 * 2016/8/15 0015
 * <p>
 * 纯净版的adapter(没有head，footer，多layout控制 数据控制，只有点击事件)
 */
public abstract class XRvPureAdapter extends RecyclerView.Adapter<XRvViewHolder> {

    /**
     * 设置某些layout不能点击
     */
    protected final SparseArray<Integer> mNoClickLayoutList = new SparseArray<>();
    /**
     * 设置某些layout不能长按
     */
    protected final SparseArray<Integer> mNoLongClickLayoutList = new SparseArray<>();
    /**
     * 设置某些layout不能选中
     */
    protected final SparseArray<Integer> mNoFocusableLayoutList = new SparseArray<>();
    /**
     * 点击事件
     */
    protected OnItemClickListener mOnItemClickListener;
    /**
     * 长按事件
     */
    protected OnItemLongClickListener mOnItemLongClickListener;

    /**
     * Tv端或者键盘手机使用
     * item 焦点 被选中
     */
    protected OnItemFocusableListener mOnItemFocusableListener;


    @NonNull
    @Override
    public XRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final XRvViewHolder viewHolder = XRvViewHolder.createViewHolder(parent.getContext(), parent, getItemLayout(viewType));
        if (mOnItemClickListener != null) {
            //判断点击事件
            if (mNoClickLayoutList.get(getItemLayout(viewType)) == null) {
                viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = viewHolder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mOnItemClickListener.onItemClick(v, viewHolder, position);
                        }
                    }
                });
            }
        }
        if (mOnItemLongClickListener != null) {
            if (mNoLongClickLayoutList.get(getItemLayout(viewType)) == null) {
                viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = viewHolder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            return mOnItemLongClickListener.onItemLongClick(v, viewHolder, position);
                        } else {
                            return false;
                        }
                    }
                });

            }
        }
        if (mOnItemFocusableListener != null) {
            if (mNoFocusableLayoutList.get(getItemLayout(viewType)) == null) {
                //todo 焦点事件，会触发两次，一次是item离开的时候，一次是item被进入的时候，
                viewHolder.getConvertView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        int position = viewHolder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mOnItemFocusableListener.onItemFocusable(v, hasFocus, viewHolder, position);
                        }
                    }
                });
            }
        }
        return viewHolder;
    }


    /**
     * 设置item layout
     *
     * @param viewType 类型
     */
    @LayoutRes
    protected abstract int getItemLayout(int viewType);


    /**
     * 添加不能点击item layout
     */
    protected void addNoClickLayout(@LayoutRes int layout) {
        mNoClickLayoutList.put(layout, layout);
    }

    /**
     * 添加不能长按的item layout
     */
    protected void addNoLongClickLayout(@LayoutRes int layout) {
        mNoLongClickLayoutList.put(layout, layout);
    }

    /**
     * 添加没有焦点 事件 的 item layout
     */
    protected void addNoFocusableLayout(@LayoutRes int layout) {
        mNoFocusableLayoutList.put(layout, layout);
    }

    /**
     * 添加 没有点击 长按 焦点 事件的layout
     */
    protected void addNoListenerLayout(@LayoutRes int layout) {
        mNoClickLayoutList.put(layout, layout);
        mNoLongClickLayoutList.put(layout, layout);
        mNoFocusableLayoutList.put(layout, layout);
    }

    /**
     * 点击接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
    }


    /**
     * 长按接口
     */
    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    /**
     * Tv端或者键盘手机使用
     * 焦点选中接口
     */
    public interface OnItemFocusableListener {
        /**
         * @param view     被选中的view
         * @param hasFocus 是否有焦点
         * @param holder   holder
         * @param position 下标
         *                 todo 焦点事件，会触发两次，一次是item离开的时候，一次是item被进入的时候，
         */
        void onItemFocusable(View view, boolean hasFocus, RecyclerView.ViewHolder holder, int position);
    }

    /**
     * 点击事件
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 长按事件
     *
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 焦点选中时间
     */
    public void setOnItemFocusableListener(OnItemFocusableListener onItemFocusableListener) {
        this.mOnItemFocusableListener = onItemFocusableListener;
    }
}

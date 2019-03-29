package com.archeanx.lib.adapter;

import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.archeanx.lib.adapter.xutil.XRvViewHolder;
import com.archeanx.lib.adapter.xutil.XRvWrapperUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xz
 * 状态Adapter
 */
public abstract class XRvStatusAdapter<T> extends XRvPureDataAdapter<T> {

    /**
     * 状态layout的 标识码
     */
    private static final int ITEM_STATUS = 9999999;

    /**
     * 没有任何加载状态
     */
    private static final int ITEM_STATUS_DEFAULT = 0;

    /**
     * 加载中
     */
    private static final int ITEM_STATUS_LOADING = 1;

    /**
     * 加载数据为空
     */
    private static final int ITEM_STATUS_EMPTY = 2;

    /**
     * 加载错误
     */
    private static final int ITEM_STATUS_ERROR = 3;

    /**
     * 标记状态码
     */
    private int mItemStatusIndex = ITEM_STATUS_DEFAULT;

    /**
     * 标题
     */
    private String mStatusTip = "";

    /**
     * 副标题
     */
    private String mStatusSubTip = "";


    /**
     * 加载事件点击
     */
    private OnStatusItemClickListener mOnStatusItemClickListener;

    /**
     * 设置 加载数据为空状态时的副标题
     */
    private String getEmptyStatusSubTip() {
        return "";
    }

    /**
     * 设置主标题文字 颜色
     */
    @ColorInt
    protected int getStatusTextColor() {
        return 0;
    }

    /**
     * 设置副标题文字 颜色
     * resouce
     */
    @ColorInt
    protected int getStatusSubTextColor() {
        return 0;
    }

    /**
     * 设置主标题文字大小
     * dp
     */
    protected int getStatusTextSize() {
        return 0;
    }

    /**
     * 设置副标题文字大小
     */
    protected int getStatusSubTextSize() {
        return 0;
    }

    /**
     * 设置状态进度条宽
     * dp
     */
    protected int getStatusProgressWidth() {
        return 0;
    }

    /**
     * 设置状态进度条高
     * dp
     */
    protected int getStatusProgressHeight() {
        return 0;
    }

    /**
     * 设置加载样式 是否填充屏幕
     * [true-填充]
     */
    protected boolean getStatusLayoutIsMatch() {
        return true;
    }

    /**
     * 初始化一个实例给状态adapter,避免mData为null，或者空
     *
     * @return 获取实例
     */
    protected abstract T initStatusLayout();

    /**
     * 显示正在加载中
     */
    public void showLoading(boolean loading) {
        if (loading) {
            mItemStatusIndex = ITEM_STATUS_LOADING;
            if (mDatas == null) {
                mDatas = new ArrayList<>();
            }
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
            mDatas.add(initStatusLayout());
            mStatusTip = "加载中...";
            mStatusSubTip="";
            notifyDataSetChanged();
        } else {
            mItemStatusIndex = ITEM_STATUS_DEFAULT;
            if (mDatas != null) {
                mDatas.clear();
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 显示数据为空
     */
    public void showEmpty(boolean empty) {
        if (empty) {
            mItemStatusIndex = ITEM_STATUS_EMPTY;
            if (mDatas == null) {
                mDatas = new ArrayList<>();
            }
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
            mDatas.add(initStatusLayout());
            mStatusTip = "暂无数据...";
            mStatusSubTip = getEmptyStatusSubTip();
            notifyDataSetChanged();
        } else {
            mItemStatusIndex = ITEM_STATUS_DEFAULT;
            if (mDatas != null) {
                mDatas.clear();
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 显示加载错误
     */
    public void showError(boolean error) {
        if (error) {
            mItemStatusIndex = ITEM_STATUS_ERROR;
            if (mDatas == null) {
                mDatas = new ArrayList<>();
            }
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
            mDatas.add(initStatusLayout());
            mStatusTip = "加载失败,请检查网络!";
            mStatusSubTip = "";
            notifyDataSetChanged();
        } else {
            mItemStatusIndex = ITEM_STATUS_DEFAULT;
            if (mDatas != null) {
                mDatas.clear();
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 显示加载错误
     */
    public void showError(boolean error, @NonNull String msg, @NonNull String subTitle) {
        if (error) {
            mItemStatusIndex = ITEM_STATUS_ERROR;
            if (mDatas == null) {
                mDatas = new ArrayList<>();
            }
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
            mDatas.add(initStatusLayout());
            mStatusTip = msg;
            mStatusSubTip = subTitle;
            notifyDataSetChanged();
        } else {
            mItemStatusIndex = ITEM_STATUS_DEFAULT;
            if (mDatas != null) {
                mDatas.clear();
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setDatas(List<T> datas) {
        mItemStatusIndex = ITEM_STATUS_DEFAULT;
        super.setDatas(datas);
        if (mDatas.size() == 0) {
            showEmpty(true);
        }
    }

    @Override
    public void setDatas(List<T> datas, boolean isRefresh) {
        mItemStatusIndex = ITEM_STATUS_DEFAULT;
        super.setDatas(datas, isRefresh);
        if (mDatas.size() == 0) {
            showEmpty(true);
        }
    }

    @Override
    public void addData(T data, boolean isRefresh) {
        mItemStatusIndex = ITEM_STATUS_DEFAULT;
        super.addData(data, isRefresh);
    }

    @Override
    public void addDatas(List<T> data, boolean isRefresh) {
        mItemStatusIndex = ITEM_STATUS_DEFAULT;
        super.addDatas(data, isRefresh);
    }


    @Override
    public final int getItemViewType(int position) {
        if (mItemStatusIndex == ITEM_STATUS_ERROR || mItemStatusIndex == ITEM_STATUS_EMPTY || mItemStatusIndex == ITEM_STATUS_LOADING) {
            return ITEM_STATUS;
        }
        return getItemViewTypeToStatus(position);
    }

    public int getItemViewTypeToStatus(int position) {
        return 0;
    }

    @Override
    public final int getItemLayout(int viewType) {
        if (viewType == ITEM_STATUS) {
            if (getStatusLayoutIsMatch()) {
                //这里不允许布局 被监听点击事件，本类自己处理点击事件
                addNoClickLayout(R.layout.adapter_item_adapter_other);
                addNoFocusableLayout(R.layout.adapter_item_adapter_other);
                addNoLongClickLayout(R.layout.adapter_item_adapter_other);
                return R.layout.adapter_item_adapter_other;
            } else {
                //这里不允许布局 被监听点击事件，本类自己处理点击事件
                addNoClickLayout(R.layout.adapter_item_adapter_other_wrap);
                addNoFocusableLayout(R.layout.adapter_item_adapter_other_wrap);
                addNoLongClickLayout(R.layout.adapter_item_adapter_other_wrap);
                return R.layout.adapter_item_adapter_other_wrap;
            }
        }
        return getItemLayoutToStatus(viewType);
    }

    /**
     * 设置layout
     */
    @LayoutRes
    public abstract int getItemLayoutToStatus(int viewType);


    @Override
    public final void onBindViewHolder(@NonNull XRvViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_STATUS) {
            ProgressBar progressBar = holder.getView(R.id.ie_progressBar);
            if (getStatusProgressHeight() != 0 && getStatusProgressWidth() != 0) {
                ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
                layoutParams.height = getStatusProgressHeight();
                layoutParams.width = getStatusProgressWidth();
                progressBar.setLayoutParams(layoutParams);
            }
            holder.setText(R.id.ie_title, mStatusTip);
            holder.setText(R.id.ie_sub_title, mStatusSubTip);

            if (mItemStatusIndex == ITEM_STATUS_LOADING) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }

            if (getStatusTextColor() != 0) {
                holder.setTextColor(R.id.ie_title, getStatusTextColor());
            }
            if (getStatusTextSize() > 0) {
                holder.setTextSize(R.id.ie_title, getStatusTextSize());
            }

            if (getStatusSubTextColor() != 0) {
                holder.setTextColor(R.id.ie_sub_title, getStatusTextColor());
            }
            if (getStatusSubTextSize() > 0) {
                holder.setTextSize(R.id.ie_sub_title, getStatusTextSize());
            }
            if (mOnStatusItemClickListener != null) {
                //这里单独处理点击事件
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemStatusIndex == ITEM_STATUS_LOADING) {
                            mOnStatusItemClickListener.onStatusLoadingClick();
                        } else if (mItemStatusIndex == ITEM_STATUS_EMPTY) {
                            mOnStatusItemClickListener.onStatusEmptyClick();
                        } else if (mItemStatusIndex == ITEM_STATUS_ERROR) {
                            mOnStatusItemClickListener.onStatusErrorClick();
                        }
                    }
                });
            }

        } else {
            onBindViewHolderToStatus(holder, position);
        }
    }


    /**
     * 显示数据,绑定控件
     */
    protected abstract void onBindViewHolderToStatus(@NonNull XRvViewHolder holder, int position);


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        XRvWrapperUtils.onAttachedToRecyclerView(recyclerView, new XRvWrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (mItemStatusIndex == ITEM_STATUS_ERROR || mItemStatusIndex == ITEM_STATUS_EMPTY || mItemStatusIndex == ITEM_STATUS_LOADING) {
                    return gridLayoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }


    @Override
    public void onViewAttachedToWindow(@NonNull XRvViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (mItemStatusIndex == ITEM_STATUS_ERROR || mItemStatusIndex == ITEM_STATUS_EMPTY || mItemStatusIndex == ITEM_STATUS_LOADING) {
            XRvWrapperUtils.setFullSpan(holder);
        }
    }

    /**
     * 设置对状态的监听
     */
    public void setOnStatusItemClickListener(OnStatusItemClickListener listener) {
        this.mOnStatusItemClickListener = listener;
    }

    /**
     * 点击接口
     */
    public static abstract class OnStatusItemClickListener {
        /**
         * 加载错误的点击
         */
        public abstract void onStatusErrorClick();

        /**
         * 加载 数据为空的点击
         */
        public void onStatusEmptyClick() {

        }

        /**
         * 加载中的点击
         */
        public void onStatusLoadingClick() {

        }
    }

}

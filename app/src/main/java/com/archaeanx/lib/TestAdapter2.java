package com.archaeanx.lib;

import android.support.annotation.NonNull;
import android.util.Log;

import com.archeanx.lib.adapter.XRvStatusAdapter;
import com.archeanx.lib.adapter.xutil.XRvViewHolder;

/**
 * @创建者
 * @创建时间
 * @描述
 */
public class TestAdapter2 extends XRvStatusAdapter<Object> {
    private static final int ITEM_1 = 111;
    private static final int ITEM_2 = 222;
    private static final int ITEM_3 = 333;
    private static final int ITEM_4 = 444;
    private static final int ITEM_5 = 555;
    private static final int ITEM_6 = 666;
    private static final int ITEM_7 = 777;


    @Override
    public void onViewRecycled(@NonNull XRvViewHolder holder) {
        Log.e("adapter_onViewRecycled_", holder.getAdapterPosition() + "");
        super.onViewRecycled(holder);
    }

    @Override
    protected String initStatusLayout() {
        return "";
    }

    @Override
    public int getItemViewTypeToStatus(int position) {
        return (int) mDatas.get(position);
    }

    @Override
    public int getItemLayoutToStatus(int viewType) {
        switch (viewType) {
            case ITEM_1:
                return R.layout.item_1;
            case ITEM_2:
                return R.layout.item_2;
            case ITEM_3:
                return R.layout.item_3;
            case ITEM_4:
                return R.layout.item_4;
            case ITEM_5:
                return R.layout.item_5;
            case ITEM_6:
                return R.layout.item_6;
            case ITEM_7:
                return R.layout.item_7;
            default:
                return R.layout.item_1;
        }
    }

    @Override
    protected void onBindViewHolderToStatus(@NonNull XRvViewHolder holder, int position) {

        switch (getItemViewTypeToStatus(position)) {
            case ITEM_1:
                holder.setText(R.id.item1, "item1_" + position);
                break;
            case ITEM_2:
                holder.setText(R.id.item2, "item3_" + position);
                break;
            case ITEM_3:
                holder.setText(R.id.item3, "item4_" + position);
                break;
            case ITEM_4:
                holder.setText(R.id.item4, "item4_" + position);
                break;
            case ITEM_5:
                holder.setText(R.id.item5, "item5_" + position);
                break;
            case ITEM_6:
                holder.setText(R.id.item6, "item6_" + position);
                break;
            case ITEM_7:
                holder.setText(R.id.item7, "item7_" + position);
                break;
        }
    }
}

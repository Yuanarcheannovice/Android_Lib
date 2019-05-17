package com.archaeanx.lib;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者
 * @创建时间
 * @描述
 */
public class TestAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_1 = 111;
    private static final int ITEM_2 = 222;
    private static final int ITEM_3 = 333;
    private static final int ITEM_4 = 444;
    private static final int ITEM_5 = 555;
    private static final int ITEM_6 = 666;
    private static final int ITEM_7 = 777;

    private List<Object> mDatas = new ArrayList<>();

    public void setDatas(List<Object> datas,boolean is) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        Log.e("adapter_onRecycled_", holder.getAdapterPosition() + "");
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemViewType(int position) {

        return (int) mDatas.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.e("adapter_onCreate_", viewType + "");
        switch (viewType) {
            case ITEM_1:
                ViewHolder1 viewHolder1 = new ViewHolder1(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_1, viewGroup, false));
                return viewHolder1;
            case ITEM_2:
                return new ViewHolder2(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_2, viewGroup, false));
            case ITEM_3:
                ViewHolder3 viewHolder3 = new ViewHolder3(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_3, viewGroup, false));
                return viewHolder3;
            case ITEM_4:
                return new ViewHolder4(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_4, viewGroup, false));
            case ITEM_5:
                return new ViewHolder5(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_5, viewGroup, false));
            case ITEM_6:
                return new ViewHolder6(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_6, viewGroup, false));
            case ITEM_7:
                return new ViewHolder7(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_7, viewGroup, false));
            default:
                return new ViewHolder1(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_1, viewGroup, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_1:
                ((ViewHolder1)viewHolder).tv.setText("item1_" + position);
                break;
            case ITEM_2:
                ((ViewHolder2)viewHolder).tv.setText("item2_" + position);
                break;
            case ITEM_3:
                ((ViewHolder3)viewHolder).tv.setText("item3_" + position);
                break;
            case ITEM_4:
                ((ViewHolder4)viewHolder).tv.setText("item4_" + position);
                break;
            case ITEM_5:
                ((ViewHolder5)viewHolder).tv.setText("item5_" + position);
                break;
            case ITEM_6:
                ((ViewHolder6)viewHolder).tv.setText("item6_" + position);
                break;
            case ITEM_7:
                ((ViewHolder7)viewHolder).tv.setText("item7_" + position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    static class ViewHolder1 extends RecyclerView.ViewHolder {

        TextView tv;

        public ViewHolder1(View view) {
            super(view);
            tv = view.findViewById(R.id.item1);
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder {

        TextView tv;

        public ViewHolder2(View view) {
            super(view);
            tv = view.findViewById(R.id.item2);
        }
    }

    static class ViewHolder3 extends RecyclerView.ViewHolder {

        TextView tv;

        public ViewHolder3(View view) {
            super(view);
            tv = view.findViewById(R.id.item3);
        }
    }

    static class ViewHolder4 extends RecyclerView.ViewHolder {

        TextView tv;

        public ViewHolder4(View view) {
            super(view);
            tv = view.findViewById(R.id.item4);
        }
    }

    static class ViewHolder5 extends RecyclerView.ViewHolder {

        TextView tv;

        public ViewHolder5(View view) {
            super(view);
            tv = view.findViewById(R.id.item5);
        }
    }

    static class ViewHolder6 extends RecyclerView.ViewHolder {

        TextView tv;

        public ViewHolder6(View view) {
            super(view);
            tv = view.findViewById(R.id.item6);
        }
    }

    static class ViewHolder7 extends RecyclerView.ViewHolder {

        TextView tv;

        public ViewHolder7(View view) {
            super(view);
            tv = view.findViewById(R.id.item7);
        }
    }
}

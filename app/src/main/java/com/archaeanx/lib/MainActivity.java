package com.archaeanx.lib;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.archeanx.lib.adapter.XRvStatusAdapter;
import com.archeanx.lib.adapter.xutil.XRvViewHolder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testAdapter adapter = new testAdapter();
        adapter.setOnStatusItemClickListener(new testAdapter.OnStatusItemClickListener() {
            @Override
            public void onStatusErrorClick() {

            }
        });

    }

    public static class testAdapter extends XRvStatusAdapter<String> {

        @Override
        protected String initStatusLayout() {
            return "";
        }

        @Override
        public int getItemLayoutToStatus(int viewType) {
            return R.layout.activity_main;
        }

        @Override
        protected void onBindViewHolderToStatus(@NonNull XRvViewHolder holder, int position) {

        }
    }
}

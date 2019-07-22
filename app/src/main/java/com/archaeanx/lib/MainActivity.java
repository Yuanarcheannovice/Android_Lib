package com.archaeanx.lib;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.archeanx.lib.adapter.XRvStatusAdapter;
import com.archeanx.lib.adapter.xutil.XRvViewHolder;
import com.archeanx.lib.appupdate.AppUpdateManager;
import com.archeanx.lib.appupdate.OnAppUpdateStatusListener;
import com.archeanx.lib.util.DpToUtil;
import com.archeanx.lib.util.ToastUtil;
import com.archeanx.lib.widget.dialog.AppProgressDialog_;
import com.archeanx.lib.widget.divider.XRvVerDivider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int ITEM_1 = 111;
    private static final int ITEM_2 = 222;
    private static final int ITEM_3 = 333;
    private static final int ITEM_4 = 444;
    private static final int ITEM_5 = 555;
    private static final int ITEM_6 = 666;
    private static final int ITEM_7 = 777;

    static List<Object> lists = new ArrayList<>();

    static {
        lists.add(ITEM_1);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_2);
        lists.add(ITEM_3);
        lists.add(ITEM_4);
        lists.add(ITEM_4);
        lists.add(ITEM_4);
        lists.add(ITEM_4);
        lists.add(ITEM_4);
        lists.add(ITEM_4);
        lists.add(ITEM_4);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_5);
        lists.add(ITEM_6);
        lists.add(ITEM_6);
        lists.add(ITEM_6);
        lists.add(ITEM_6);
        lists.add(ITEM_6);
        lists.add(ITEM_6);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
        lists.add(ITEM_7);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUpdateManager.getInstance().init(this);
        Log.e("showLogn_begin", Long.toString(System.currentTimeMillis()));
        ToastUtil.init(this);
        ToastUtil.show("aljfsdlfjalsjljljljagl;difjasnc");
        Log.e("showLogn_endee", Long.toString(System.currentTimeMillis()));

        findViewById(R.id.am_show_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUpdateManager.getInstance().inspectVersion( "更新App", "http://news.wisdomforcloud.com/17.apk");
            }
        });

        RecyclerView recyclerView = findViewById(R.id.am_rv);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new XRvVerDivider.Builder(this).build());
        TestAdapter1 adapter = new TestAdapter1();

        recyclerView.setAdapter(adapter);

        adapter.setDatas(lists, true);

        AppUpdateManager.getInstance().setOnAppUpdateStatusListener(new OnAppUpdateStatusListener() {

            @Override
            public void onRunning() {
                ToastUtil.show("正在下载App更新包");
            }
        });


        AppProgressDialog_.show(this,"加载中");
    }


}
